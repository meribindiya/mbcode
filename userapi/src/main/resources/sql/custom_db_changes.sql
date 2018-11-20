INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('MAX_UNREGISTERED_CONTACTS', '20');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('NETWORK_DEPTH', '5');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('REFERRAL_ON_SELF_REGISTRATION', '100');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('REFERRAL_ON_REFERRED_FIRST_ORDER', '50');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('DEFAULT_WALLET_AMOUNT_ON_CREATION', '0');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('NETWORK_COMMISION_PERCENTAGE', '1');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('ACCOUNT_ACTIVE_AMT_CRITERIA', '1500');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('ACCOUNT_ACTIVE_DAYS_CRITERIA', '90');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('GOLD_MEMBER_ACTIVE_USER_CRITERIA', '1000');
INSERT INTO `mb`.`tbl_constants` (`property`, `value`) VALUES ('USER_APP_ALIVE_DAYS_LIMIT', '1');



-- addMoneyToWallet function

DELIMITER $$
 DROP FUNCTION IF EXISTS mb.addMoneyToWallet;
CREATE FUNCTION mb.addMoneyToWallet(_userId INT, _amount double, _transactionType TEXT) RETURNS INT
    DETERMINISTIC
	BEGIN
		DECLARE _walletId int default 0;
		 -- create wallet for this user and add wallet amount.
		INSERT INTO mb.tbl_user_wallet (amount, userId) VALUES (_amount, _userId) ON DUPLICATE KEY UPDATE amount = ROUND((amount+_amount), 2);

		-- get new wallet id
		SELECT id into _walletId FROM mb.tbl_user_wallet WHERE userId = _userId;

		-- add wallet transaction.
		INSERT INTO mb.tbl_user_wallet_transaction (walletId, amount, walletTransactionType) VALUES (_walletId, _amount, _transactionType);
    RETURN _walletId;
	END$$
DELIMITER ;

-- add user to network table and handle wallet referrals etc
DELIMITER $$

DROP TRIGGER IF EXISTS mb.after_user_insert;
CREATE TRIGGER mb.after_user_insert
   AFTER INSERT ON tbl_user
   FOR EACH ROW
BEGIN

	  DECLARE _referrerId INT DEFAULT 0;
    DECLARE _referralOnSelfregistration double DEFAULT 0;
    DECLARE _defaultWalletAmountOnCreation double default 0;
    DECLARE _walletId int default 0;

    DECLARE _wallettransactionType TEXT default "";
    DECLARE _walletAmount double default 0;

     -- GET referId from refer contacts if this user was shared by some another user.
     SELECT referrerId INTO _referrerId FROM mb.tbl_refer_contacts WHERE mobile = NEW.mobile;
	  INSERT INTO mb.tbl_user_network (parentId, userId) VALUES (_referrerId, NEW.user_id);

    -- wallet handling
    -- get constants
    SELECT value FROM mb.tbl_constants WHERE property = 'REFERRAL_ON_SELF_REGISTRATION' INTO _referralOnSelfregistration;
    SELECT value FROM mb.tbl_constants WHERE property = 'DEFAULT_WALLET_AMOUNT_ON_CREATION' INTO _defaultWalletAmountOnCreation;


    -- check if this user has been referred by some referrer
    IF (_referrerId != 0) THEN
		  SET _walletAmount = _referralOnSelfregistration;
      SET _wallettransactionType = 'IN_REFERRAL_ON_SELF_REGISTRATION';
	  ELSE
		  SET _walletAmount = _defaultWalletAmountOnCreation;
      SET _wallettransactionType = 'IN_DEFAULT_WALLET_AMOUNT_ON_CREATION';
	  END IF;

   -- create wallet for this user and add wallet amount.
  SELECT mb.addMoneyToWallet (NEW.user_id, _walletAmount, _wallettransactionType) INTO _walletId;

END$$

DELIMITER ;

-- after order update to handle user activate/beauty network

DELIMITER $$

DROP TRIGGER IF EXISTS mb.after_order_update;
CREATE TRIGGER mb.after_order_update
   AFTER UPDATE ON mb.tbl_order
   FOR EACH ROW
BEGIN

	DECLARE _parentId INT DEFAULT 0;
    DECLARE _orderCount INT DEFAULT 0;
    DECLARE _referralOnReferredFirstOrder double default 0;
    DECLARE _walletId int default 0;
    DECLARE _finished int default 0;

	IF (OLD.order_status_code != 3 AND NEW.order_status_code = 3) THEN
		-- get constants
		SELECT value FROM	mb.tbl_constants WHERE property = 'REFERRAL_ON_REFERRED_FIRST_ORDER' INTO _referralOnReferredFirstOrder;

		-- marking user activated user.
		UPDATE mb.tbl_user SET isActivated = 1 WHERE user_id = NEW.userid;

		-- check if this user has any parent
		SELECT parentId	INTO _parentId FROM	mb.tbl_user_network	WHERE	userId = NEW.userid;

		IF _parentId != 0 THEN
			-- mark parent beauty network activated user
			UPDATE mb.tbl_user SET isBeautyNetworkActivated = 1 WHERE user_id = _parentId;

			-- check if this is first order
			SELECT COUNT(*)	INTO _orderCount FROM	mb.tbl_order WHERE userId = NEW.userid and order_status_code = 3;

			IF _orderCount = 1 THEN
				-- add referral amount to parent user's wallet on first order of referred user.
				SELECT mb.addMoneyToWallet (_parentId, _referralOnReferredFirstOrder, 'IN_REFERRAL_ON_REFERRED_FIRST_ORDER') INTO _walletId;
			END IF;
		END IF;
		SELECT mb.handleCommisionEarnings (NEW.userid, NEW.id, NEW.total) INTO _finished;
  END IF;
  IF (OLD.order_status_code != 10 AND NEW.order_status_code = 10) THEN
    IF (OLD.paidWithWallet > 0) THEN
		  SELECT mb.addMoneyToWallet (OLD.userid, OLD.paidWithWallet, 'IN_ORDER_CANCELLED_REVERSAL') INTO _walletId;
    END IF;
  END IF;
END$$

DELIMITER ;

-- after order insert. handle wallet amount and wallet transactions.
DELIMITER $$

DROP TRIGGER IF EXISTS mb.after_order_insert;
CREATE TRIGGER mb.after_order_insert
   AFTER INSERT ON mb.tbl_order
   FOR EACH ROW
BEGIN

    DECLARE _amountPaidWithWallet double default 0;
    DECLARE _walletId INT default 0;

    SET _amountPaidWithWallet = NEW.paidWithWallet * -1;

	IF (NEW.paidWithWallet > 0) THEN
		SELECT mb.addMoneyToWallet (NEW.userid, _amountPaidWithWallet, 'OUT_ORDER_PAYMENT') INTO _walletId;
  END IF;
END$$

DELIMITER ;

-- handle commision transactions
DELIMITER $$
DROP FUNCTION IF EXISTS mb.handleCommisionEarnings;
CREATE FUNCTION mb.handleCommisionEarnings(_userId INT, _orderId INT, _total DOUBLE) RETURNS INT
DETERMINISTIC
  BEGIN

    DECLARE _parentId INT DEFAULT 0;
    DECLARE _isAccountActive BIT DEFAULT 0;
    DECLARE _isAppAlive BIT DEFAULT 0;
    DECLARE _commisionPercentage double default 0;
    DECLARE finished INT DEFAULT 0;
    DECLARE _temp INT DEFAULT 0;
    -- GET all parent users with activated account.
    DECLARE _cursor CURSOR FOR select user_id, mb.isAccountActive(user_id), mb.isAppAlive(user_id) FROM mb.tbl_user WHERE FIND_IN_SET (user_id, mb.getParentsInNetwork(_userId));
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;

     -- get constants
     SELECT value FROM mb.tbl_constants WHERE property = 'NETWORK_COMMISION_PERCENTAGE' INTO _commisionPercentage;

    OPEN _cursor;

    commision: LOOP
      FETCH _cursor INTO _parentId, _isAccountActive, _isAppAlive;

      IF finished = 1 THEN
        LEAVE commision;
      END IF;
      -- add commision money to wallet if account is active and app is alive
      IF (_isAccountActive = 1 AND _isAppAlive =1) THEN
        SELECT mb.addMoneyToWallet (_parentId, ROUND(_total*_commisionPercentage/100, 2), 'IN_EARNING') INTO _temp;
        INSERT INTO mb.tbl_user_earning (userId, amount, isMissed, orderId) VALUES (_parentId, ROUND(_total*_commisionPercentage/100, 2), 0, _orderId);
      ELSE
        INSERT INTO mb.tbl_user_earning (userId, amount, isMissed, orderId) VALUES (_parentId, ROUND(_total*_commisionPercentage/100, 2), 1, _orderId);
      END IF;
    END LOOP commision;

    CLOSE _cursor;

    RETURN finished;

  END$$
DELIMITER ;
-- get parents in network procedure.

DELIMITER $$
DROP PROCEDURE IF EXISTS mb.getParentsInNetwork;
CREATE PROCEDURE mb.getParentsInNetwork( IN _userId INT, IN _depth INT, OUT res Text)
  BEGIN
    DECLARE _parentId INT DEFAULT 0;
    DECLARE _parents TEXT DEFAULT "";
    SELECT parentId into _parentId FROM mb.tbl_user_network WHERE userId = _userId;
    SET _depth = _depth -1;
    IF _parentId != 0 AND _depth >= 0 THEN
      CALL mb.getParentsInNetwork (_parentId, _depth, _parents);
      SELECT CONCAT (_parentId, ",", _parents) INTO _parents;
    END IF;
    SELECT TRIM(TRAILING ',' FROM _parents) into res;
  END$$
DELIMITER ;

-- get parents in network function.
DELIMITER $$
 DROP FUNCTION IF EXISTS mb.getParentsInNetwork;
CREATE FUNCTION mb.getParentsInNetwork(_userId INT) RETURNS TEXT
    DETERMINISTIC
	BEGIN
   DECLARE _parents TEXT DEFAULT "";
   DECLARE _depth INT DEFAULT 0;
   SET max_sp_recursion_depth = 20;
    -- get constants
   SELECT value FROM mb.tbl_constants WHERE property = 'NETWORK_DEPTH' INTO _depth;
   CALL mb.getParentsInNetwork (_userId, _depth, _parents);
   RETURN _parents;
	END$$
DELIMITER ;


-- function to check is user accounts is active and he is eligible to earn from referral's orders.

DELIMITER $$
DROP FUNCTION IF EXISTS mb.isAccountActive;
CREATE FUNCTION mb.isAccountActive(_userId INT) RETURNS BIT
    DETERMINISTIC

	BEGIN
    DECLARE _accActiveAmtCriteria double DEFAULT 0;
    DECLARE _accActiveDaysCriteria double DEFAULT 0;
    DECLARE _total double DEFAULT 0;
    DECLARE _maxTotal double DEFAULT 0;
    DECLARE _isAccountActive bit DEFAULT 0;
		-- get constants
SELECT
    value
FROM
    mb.tbl_constants
WHERE
    property = 'ACCOUNT_ACTIVE_AMT_CRITERIA' INTO _accActiveAmtCriteria;
SELECT
    value
FROM
    mb.tbl_constants
WHERE
    property = 'ACCOUNT_ACTIVE_DAYS_CRITERIA' INTO _accActiveDaysCriteria;

    SELECT MAX(total) INTO _maxTotal FROM mb.tbl_order where userid = _userId
		and order_status_code = 3;

	IF (_maxTotal >= _accActiveAmtCriteria) THEN
      SELECT sum(total) INTO _total FROM mb.tbl_order where userid = _userId
      and order_status_code = 3 and TIMESTAMPDIFF(DAY, booking_date, CURDATE()) <=_accActiveDaysCriteria;

      IF _total >= _accActiveAmtCriteria THEN
        SET _isAccountActive =  1;
      ELSE
        SET _isAccountActive =  0;
      END IF;
	ELSE
		  SET _isAccountActive =  0;
	END IF;

    RETURN _isAccountActive;
	END$$
DELIMITER ;


-- function to check if app is alive at user's device;
DELIMITER $$
DROP FUNCTION IF EXISTS mb.isAppAlive;
CREATE FUNCTION mb.isAppAlive(_userId INT) RETURNS INT
DETERMINISTIC
  BEGIN

  DECLARE _isAppAlive INT DEFAULT 0;
  DECLARE _userAppAliveDaysLimit double DEFAULT 0;

  -- get constants
  SELECT value FROM mb.tbl_constants WHERE property = 'USER_APP_ALIVE_DAYS_LIMIT' INTO _userAppAliveDaysLimit;

  SELECT count(*) INTO _isAppAlive FROM mb.tbl_user where user_id = _userId and isAppAlive = 1 and TIMESTAMPDIFF(DAY, appAliveUpdatedAt, CURDATE()) <= _userAppAliveDaysLimit;

  RETURN _isAppAlive;

  END$$
DELIMITER ;