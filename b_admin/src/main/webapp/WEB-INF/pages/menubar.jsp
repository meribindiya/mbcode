<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<div class="left-sidebar">
            <!-- Sidebar scroll-->
            <div class="scroll-sidebar">
                <!-- Sidebar navigation-->
                <nav class="sidebar-nav">
                    <ul id="sidebarnav">
                        <li class="nav-devider"></li>
                        <li class="nav-label">Home</li>
                        <li> <a href="${pageContext.request.contextPath}/dashboard" aria-expanded="false">Dashboard </a>
                        </li>
                        <li class="nav-label">Employee </li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Employee</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/employee/add">Add </a></li>
                                <li><a href="${pageContext.request.contextPath}/employee/all">View And Manage</a></li>
                            </ul>
                        </li>
                         <li class="nav-label">App Manage </li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Category</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/category/add">Add </a></li>
                                <li><a href="${pageContext.request.contextPath}/category/all">View And Manage</a></li>
                            </ul>
                        </li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Sub Category</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/subcategory/add">Add </a></li>
                                <li><a href="${pageContext.request.contextPath}/subcategory/all">View And Manage</a></li>
                            </ul>
                        </li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Services</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/service/add">Add </a></li>
                                <li><a href="${pageContext.request.contextPath}/service/all">View And Manage</a></li>
                            </ul>
                        </li>
                          <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Banners</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/banner/upload">Add </a></li>
                                <li><a href="${pageContext.request.contextPath}/banner/all">View All Banners </a></li>
                            </ul>
                        </li>
                         <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Notification</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/notification">Send Notification </a></li>
                                <li><a href="${pageContext.request.contextPath}/notification/all">View All</a></li>
                            </ul>
                        </li>
                        <li class="nav-label">Orders</li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Orders</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/orders">Orders</a></li>
                                <li><a href="${pageContext.request.contextPath}/orders/book-order">Book Order</a></li>
                            </ul>
                        </li>
                        
                         <li class="nav-label">Customers</li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Customer</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/customers">Customers</a></li>
                            </ul>
                        </li>
                        
                         <li class="nav-label">Beauticians</li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Beautician</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/beautician/add">Add </a></li>
                                <li><a href="${pageContext.request.contextPath}/beautician/all">View And Manage</a></li>
                            </ul>
                        </li>
                         <li class="nav-label">Report</li>
                        <li> <a class="has-arrow  " href="#" aria-expanded="false"><i class="fa fa-columns"></i><span class="hide-menu">Report</span></a>
                            <ul aria-expanded="false" class="collapse">
                                <li><a href="${pageContext.request.contextPath}/report">Report</a></li>
                            </ul>
                        </li>
                    </ul>
                </nav>
                <!-- End Sidebar navigation -->
            </div>
            <!-- End Sidebar scroll-->
        </div>