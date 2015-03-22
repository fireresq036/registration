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
<fmt:formatDate pattern="MM/dd/yyyy" value="${edit_event.endDate}"
                var="formatedEndDate"/>
<fmt:formatDate pattern="MM/dd/yyyy" value="${edit_event.startDate}"
                var="formatedStartDate"/>
</p>
<form method="post" id="event_form"
  <c:if test="${add_event}">
      action="/admin?command=add_event"
  </c:if>
  <c:if test="${!add_event}">
      action="/admin?command=edit_event"
  </c:if>
>
    <input id="id" type="hidden" name="eventId" value="${edit_event.id}"/>
    <div class="wrapper">
        <div id="name_div">
            Event Name:
            <input id="name" type="text" name="eventName" value="${edit_event.name}"
                   onBlur="validateString(this, 'submit_button')"/>
        </div>
        <div id="name_error"></div>
        <div id="description_div">
            Event Description:
            <input id="description" type="text" name="eventDescription" value="${edit_event.description}"
                   onBlur="validateString(this, 'submit_button')"/>
        </div>
        <div id="description_error"></div>
        <div id="location_div">
            Event Location:
            <input id="location" type="text" name="eventLocation" value="${edit_event.location}"
                   onBlur="validateString(this, 'submit_button')"/>
        </div>
        <div id="location_error"></div>
        <div id="startDate_div">
            Event Start Date (mm/dd/yyy):
            <input id="startDate" type="text" name="eventStartDate" value="${formatedStartDate}"
                   onBlur="validateDate(this, 'submit_button')"/>
        </div>
        <div id="startDate_error" ></div>
        <div id="endDate_div">
            Event End Date (mm/dd/yyy):
            <input id="endDate" type="text" name="eventEndDate" value="${formatedEndDate}"
                   onBlur="validateDate(this, 'submit_button')"/>
        </div>
        <div id="endDate_error"></div>
        <div id="teamSize_div">
            Size of Team for Event:
            <input id="teamSize" type="text" name="eventTeamSize" value="${edit_event.teamSize}"
                   onBlur="validateNumber(this, 'submit_button')"/>
        </div>
        <div id="teamSize_error" class="hidden"></div>
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