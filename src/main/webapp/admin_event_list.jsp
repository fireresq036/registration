<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="org.portersville.muddycreek.vfd.entity.Event" %>

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
  <title>Fishing Registration</title>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%@ include file="header.html" %>

<h2>Administration</h2>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);
    } else {
       // New location to be redirected
       String site = new String("welcome.html");
       response.setStatus(response.SC_FORBIDDEN);
       response.setHeader("Location", site);
    }
%>
<h3>Current Events</h3>
<table border=1>
    <tr>
        <th>Name</th>
        <th>Description</th>
        <th>Location</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Team Size</th>
    </tr>
    <c:forEach items="${known_events}" var="current_event">
        <tr>
            <td>
                <a href="/admin?command=edit_event_query&editKey=${current_event.id}">
                    ${current_event.name}
                </a>
            </td>
            <td>${current_event.description}</td>
            <td>${current_event.location}</td>
            <td>
                <fmt:formatDate pattern="yyyy-MM-dd"
                 value="${current_event.startDate}" />
            </td>
            <td>
                <fmt:formatDate pattern="yyyy-MM-dd"
                value="${current_event.endDate}" />
            </td>
            <td>${current_event.teamSize}</td>
        </tr>
    </c:forEach>
</table>
<form action="admin_event_edit.jsp" method="get">
    <input type="submit" name="command" value="Add Event"/>
</form>
<%@ include file="footer.html" %>
</body>
</html>