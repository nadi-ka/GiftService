<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
<meta charset="ISO-8859-1">
<title>Tags</title>
</head>

<body>

	Tags:

	
	<ul>
		<c:forEach var="tag" items="${tags}">
			<li>
			id: ${tag.id} / tagName: ${tag.name}
			</li>
		</c:forEach>
	</ul>

</body>
</html>