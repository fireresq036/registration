<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="org.portersville.muddycreek.vfd.entity.Address" %>
<%@ page import="org.portersville.muddycreek.vfd.entity.Person" %>
<%@ page import="org.portersville.muddycreek.vfd.entity.Team" %>

<%
Team team = (Team) request.getAttribute("team_data");
pageContext.setAttribute("capt_email", team.getCaptain().getEmail());
pageContext.setAttribute("capt_phone", team.getCaptain().getPhone());
pageContext.setAttribute("capt_street1", team.getCaptain().getAddress().getStreet1());
pageContext.setAttribute("capt_street2", team.getCaptain().getAddress().getStreet2());
boolean has_street2 = true;
if (team.getCaptain().getAddress().getStreet2().length() <= 0) {
    has_street2 = false;
}
pageContext.setAttribute("capt_city", team.getCaptain().getAddress().getCity());
pageContext.setAttribute("capt_state", team.getCaptain().getAddress().getState());
pageContext.setAttribute("capt_zip", team.getCaptain().getAddress().getZip());
%>

<head>
  <title>Fishing Registration</title>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<html>
    <body>
        <%@ include file="header.html" %>
        <h2>Registration</h2>
        <p>Please print a copy for your records</p>
        <p> Team <b>${fn:escapeXml(team_data.name)}</b></p>
        <table style="width:80%">
          <tr>
            <td style="width:10%">Captain</td>
            <td>${fn:escapeXml(team_data.captain.name)}</td>
          </tr>
          <tr>
            <td>EMail</td>
            <td>${fn:escapeXml(capt_email)}</td>
          </tr>
          <tr>
            <td>Phone</td>
            <td>${fn:escapeXml(capt_phone)}</td>
          </tr>
          <tr>
            <td>Address</td>
            <td>
                ${fn:escapeXml(capt_street1)}</br>
                <% if (has_street2) { %>
                    ${fn:escapeXml(capt_street2)}</br>
                <% } %>
                ${fn:escapeXml(capt_city)},
                ${fn:escapeXml(capt_state)}
                ${fn:escapeXml(capt_zip)}
            </td>
          </tr>
        </table>
        <%@ include file="footer.html" %>
    </body>

</html>