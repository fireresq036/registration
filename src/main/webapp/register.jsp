<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="org.portersville.muddycreek.vfd.entity.Address" %>
<%@ page import="org.portersville.muddycreek.vfd.entity.Person" %>
<%@ page import="org.portersville.muddycreek.vfd.entity.Team" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Fishing Registration</title>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%@ include file="header.html" %>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    Team team = (Team) request.getAttribute("team_data");
    if (team == null) {
        pageContext.setAttribute("team_name", "");
        pageContext.setAttribute("capt_email", user.getEmail());
        pageContext.setAttribute("capt_name", "");
        pageContext.setAttribute("capt_phone", "");
        pageContext.setAttribute("capt_street1", "");
        pageContext.setAttribute("capt_street2", "");
        pageContext.setAttribute("capt_city", "");
        pageContext.setAttribute("capt_state", "PA");
        pageContext.setAttribute("capt_zip", "");
     } else {
        pageContext.setAttribute("team_name", team.getName());
        pageContext.setAttribute("capt_name", team.getCaptain().getName());
        pageContext.setAttribute("capt_email", team.getCaptain().getEmail());
        pageContext.setAttribute("capt_phone", team.getCaptain().getPhone());
        pageContext.setAttribute("capt_street1", team.getCaptain().getAddress().getStreet1());
        pageContext.setAttribute("capt_street2", team.getCaptain().getAddress().getStreet2());
        if (team.getCaptain().getAddress().getStreet2().length() <= 0) {
            pageContext.setAttribute("capt_street2", "");
        }
        pageContext.setAttribute("capt_city", team.getCaptain().getAddress().getCity());
        pageContext.setAttribute("capt_state", team.getCaptain().getAddress().getState());
        pageContext.setAttribute("capt_zip", team.getCaptain().getAddress().getZip());
    }
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
        <input type="text" name="teamName" value="${fn:escapeXml(team_name)}"/>
    </div>
    <div>
        <p>Captain Name:
        <br/>
        <input type="text" name="captainName" value="${fn:escapeXml(capt_name)}"/>
    </div>
    <div>
        <p>Captain Email (This email address will be used fro all team contact):
        <br/>
        <input type="text" name="captainEmail" value="${fn:escapeXml(capt_email)}"/>
    </div>
    <div>
        <p>Captain Phone:
        <br/>
        <input type="text" name="captainPhone" value="${fn:escapeXml(capt_phone)}"/>
    </div>
    <div>
        <p>Snail Mail Address:
        <br/>
        <p>Address:</p></br>
        <input type="text" size=50 name="captainAddress1" value="${fn:escapeXml(capt_street1)}"/></br>
        <input type="text" size=50 name="captainAddress2" value="${fn:escapeXml(capt_street2)}"/></br>
        <p>City: </p><input type="text" size=50 name="captainCity" value="${fn:escapeXml(capt_city)}"/>
        <p>State: </p><input type="text" size=10 name="captainState" value="${fn:escapeXml(capt_state)}"/>
        <p>Zip: </p><input type="text" size=10 name="captainZip" value="${fn:escapeXml(capt_zip)}"/>
    </div>
    <div>
        <input type="submit" value="Register Team"/>
    </div>
</form>
<%@ include file="footer.html" %>
</body>
</html>