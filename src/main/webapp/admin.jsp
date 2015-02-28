<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>Fishing Registration</title>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%@ include file="header.html" %>

<h2>Administration</h2>

<h3> what do you want to do?</h3>
<a href="/admin?command=get_events">Edit Events</a>
<a href="/admin?command=get_users">Edit Users</a>
<%@ include file="footer.html" %>
</body>
</html>