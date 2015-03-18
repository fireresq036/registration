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
  <script src="js/jquery-2.1.3.min.js"></script>
  <script src="js/jquery-dateFormat.min.js"></script>
  <script src="js/form-util.js"></script>
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
       response.setStatus(response.SC_FORBIDDEN);
       response.setHeader("Location", "welcome.html");
    }
%>
<%--  onsubmit="return validate_form();" --%>
<script>
console.log("event id: ${edit_event.id}");
</script>
<fmt:formatDate type="date" value="${now}" />
<form method="post" id="event_form"
  <c:if test="${add_event}">
      action="/admin?command=add_event"
  </c:if>
  <c:if test="${!add_event}">
      action="/admin?command=edit_event&editKey=${edit_event.id}"
  </c:if>
>
    <div>
        <p>Event Name:
        <div id="name_error" class="hidden"></div>
        <input id="name" type="text" name="eventName" value="${edit_event.name}"
               onBlur="validateString(this, 'submit_button')"/>
    </div>
    <div>
        <p>Event Description:
        <div id="description_error" class="hidden"></div>
        <input id="description" type="text" name="eventDescription" value="${edit_event.description}"
               onBlur="validateString(this, 'submit_button')"/>
    </div>
    <div>
        <p>Event Location:
        <div id="location_error" class="hidden"></div>
        <input id="location" type="text" name="eventLocation" value="${edit_event.location}"
               onBlur="validateString(this, 'submit_button')"/>
    </div>
    <div>
        <p>Event Start Date (mm/dd/yyy):
        <div id="startDate_error" class="hidden"></div>
        <input id="startDate" type="text" name="eventStartDate" value="${page.startDate}"
               onBlur="validateDate(this, 'submit_button')"/>
    </div>
    <div>
        <p>Event End Date (mm/dd/yyy):
        <div id="endDate_error" class="hidden"></div>
        <input id="endDate" type="text" name="eventEndDate" value="${edit_event.endDate}"
               onBlur="validateDate(this, 'submit_button')"/>
    </div>
    <div>
        <p>Size of Team for Event:
        <div id="teamSize_error" class="hidden"></div>
        <input id="teamSize" type="text" name="eventTeamSize" value="${edit_event.teamSize}"
               onBlur="validateNumber(this, 'submit_button')"/>
    </div>
    <div>
        <c:if test="${add_event}">
            <input id="submit_button" type="submit" value="Register Event" disabled="true"/>
        </c:if>
        <c:if test="${!add_event}">
            <input id="submit_button" type="submit" value="Edit Event"  disabled="true"/>
        </c:if>
    </div>
</form>
<%@ include file="footer.html" %>
</body>
</html