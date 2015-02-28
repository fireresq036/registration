<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="org.portersville.muddycreek.vfd.registration.Registration.Event" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

    List<Event> events = (List<Event>) request.getAttribute("known_events");

    if (events.size() > 0) {
%>
<h3>Current Events</h3>
<% } else { %>
<h4>No events in the database</h4>
<% } %>
<form action="/admin?command=add_event" method="post" id="add_event_form">
    <div>
        <p>Event Name:
        <br/>
        <input type="text" name="eventName" value=""/>
    </div>
    <div>
        <p>Event Description:
        <br/>
        <input type="text" name="eventDescription" value=""/>
    </div>
    <div>
        <p>Event Location:
        <br/>
        <input type="text" name="eventLocation" value=""/>
    </div>
    <div>
        <p>Event Start Date (mm/dd/yyy):
        <br/>
        <input type="text" name="eventStartDate" value=""/>
    </div>
    <div>
        <p>Event Name:
        <br/>
        <input type="text" name="eventEndDate" value=""/>
    </div>
    <div>
        <p>Event Name:
        <br/>
        <input type="text" name="eventTeamSize" value=""/>
    </div>
    <div>
        <input type="submit" value="Register Team"/>
    </div>
</form>
<%@ include file="footer.html" %>
</body>
</html>