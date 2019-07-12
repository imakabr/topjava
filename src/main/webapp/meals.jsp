<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="color" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8"/>
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
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Edit</th>
        <th>Del</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <c:if test="${meal.excess}">
            <c:set var="color" value="<span style=\"color: red;\" />"/>
        </c:if>
        <c:if test="${!meal.excess}">
            <c:set var="color" value=""/>
        </c:if>
        <tr>
            <td>${color}
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/></td>
            <td>${color}<c:out value="${meal.description}"/></td>
            <td>${color}<c:out value="${meal.calories}"/></td>
            <td><a class="material-icons button edit"
                   href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">edit</a></td>
            <td><a class="material-icons button delete" href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">delete</a>
            </td>

        </tr>
    </c:forEach>
    <td><a class="material-icons button add"
           href="meals?action=add">add</a></td>
    </tbody>
</table>
</body>
</html>
