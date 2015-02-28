<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <title>Fishing Registration</title>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%@ include file="header.html" %>

<%
    String teamName = request.getParameter("teamName");
    if (teamName == null) {
        teamName = "";
    }
    pageContext.setAttribute("teamName", teamName);
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);
%>
        <p>Hello, ${fn:escapeXml(user.nickname)}! (If this is not you
            <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">
                sign out
            </a>.
            )
        </p>
    <%
    } else {
    %>
        <p>You must be signed into google to use this site,
            <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">
                Sign in
            </a>
            .
        </p>
    <%
    }
    %>

<form action="/register" method="post" id="register_form">
    <div>
        <p>Team Name:
        <br/>
        <input type="text" name="teamName" value="${fn:escapeXml(teamName)}"/>
    </div>
    <div>
        <p>Captain Name:
        <br/>
        <input type="text" name="captainName" value=""/>
    </div>
    <div>
        <p>Captain Email (This email address will be used fro all team contact):
        <br/>
        <input type="text" name="captainEmail" value="${fn:escapeXml(user.email)}"/>
    </div>
    <div>
        <p>Captain Phone:
        <br/>
        <input type="text" name="captainPhone" value=""/>
    </div>
    <div>
        <p>Snail Mail Address:
        <br/>
        <p>Address:</p></br>
        <input type="text" size=50 name="captainAddress1" value=""/></br>
        <input type="text" size=50 name="captainAddress2" value=""/></br>
        <p>City: </p><input type="text" size=50 name="captainCity" value=""/>
        <p>State: </p><input type="text" size=10 name="captainState" value="PA"/>
        <p>Zip: </p><input type="text" size=10 name="captainZip" value=""/>
    </div>
    <div>
        <input type="submit" value="Register Team"/>
    </div>
</form>
<%@ include file="footer.html" %>
</body>
</html>