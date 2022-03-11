<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Index view</title>
	</head>

	<body>
		This is the index view jsp

		<form action="${pageContext.servletContext.contextPath}/index" method="post">
			<input type="submit" name="titlePage" value="TitlePage!">
			<input type="submit" name="multiply" value="Multiply Numbers!">
			<input type="submit" name="guess" value="Guessing Game!">
		</form>
	</body>
</html>
