<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="color" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <title>Meals</title>
    <meta name="viewport" content="initial-scale=1.0; maximum-scale=1.0; width=device-width;">
    <link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <thead>
    <tr>
<%--        <th>Meal Id</th>--%>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Edit</th>
        <th>Delete</th>
        <th>Add</th>
<%--        <th colspan=2>Action</th>--%>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${listMealTo}" var="meal">
        <c:if test="${meal.excess}">
        <c:set var="color" value="<span style=\"color: red;\" />" />
        </c:if>
        <tr>
            <td>${color}<fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /> </td>
            <td>${color}<c:out value="${meal.description}" /></td>
            <td>${color}<c:out value="${meal.calories}" /></td>
<%--            <a href="UserController?action=edit&userId=<c:out value="${user.userid}"/>">Update</a>--%>
            <td>  <a class="material-icons button edit" href="UserController?action=edit&userId=<c:out value="${user.userid}"/>" text-decoration="none">edit</a></td>
            <td>  <a class="material-icons button delete" href="meals?action=delete&mealId=<c:out value="${meal.id}"/>" text-decoration="none">delete</a></td>
            <td>  <i class="material-icons button add">add</i></td>

<%--            <td><fmt:formatDate pattern="yyyy-MMM-dd" value="${meals.dob}" /></td>--%>
<%--            <td><a href="UserController?action=edit&userId=<c:out value="${meals.userid}"/>">Update</a></td>--%>
<%--            <td><a href="UserController?action=delete&userId=<c:out value="${meals.userid}"/>">Delete</a></td>--%>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
