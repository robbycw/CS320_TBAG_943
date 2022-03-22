<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>9:43</title>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: right;
		}
		<%@include file="titlePage.css" %>
		</style>
	
		
	<body>
		<div id="entire">
			<div id="gameTitle"  style="background-image: <img th:src=@{|/gifs/${gif.Clock}.gif|} />">
				<h1>9:43</h1>
				<img th:src="@{|/gifs/${gif.Clock}.gif|}" alt="gif" />
				<div id="buttons">
					<form action="${pageContext.servletContext.contextPath}/titlePage" method="post">
						<input type="submit" name="game" value="Play Game!">
					</form>
				</div>
				<div id="buttons">
					<form action="${pageContext.servletContext.contextPath}/titlePage" method="post">
						<input type="submit" name="options" value="Options">
					</form>
				</div>
				<div id="buttons">
					<form action="${pageContext.servletContext.contextPath}/titlePage" method="post">
						<input type="submit" name="credits" value="Credits">
					</form>
				</div>		
			</div>
			
			
			
		</div>
		
	</body>
	</head>
</html>