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
			<div id="gameTitle"  style="background-image: url('https://64.media.tumblr.com/1170193f27e19c482c3d5bd770b74cf2/b7d54b1b16923c97-3d/s540x810/76e0653190055b73ca2beba02b1d3f6b572f014e.gifv');">
				<h1>9:43</h1>
				<div id="buttons">
					<form action="${pageContext.servletContext.contextPath}/index" method="post">
						<input type="submit" name="titlePage" value="TitlePage!">
					</form>
				</div>
				<div id="buttons">
					<form action="${pageContext.servletContext.contextPath}/index" method="post">
						<input type="submit" name="multiply" value="Multiply Numbers!">
					</form>
				</div>
				<div id="buttons">
					<form action="${pageContext.servletContext.contextPath}/index" method="post">
						<input type="submit" name="guess" value="Guessing Game!">
					</form>
				</div>		
			</div>
			
			
			
		</div>
		
	</body>
	</head>
</html>