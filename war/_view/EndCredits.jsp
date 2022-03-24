<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head style="background-color:black;">
		<title style="color:red;">9:43</title>
		<style>
			button[type=submit] {
				background-color: black;
				color: red;
				padding: 16px 32px;
				position: fixed;
				bottom: 200px;
				right: 610px;
				font-size:200%;
			}
		</style>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: right;
		}
		</style>
	</head>

	<body style="background-color: black;">
		<h1 style="color: red;font-size:300%;">
			<center>-11:43-</center>
		</h1>
		<h2 style="color: red;font-size:400%;">
			<center>GAME OVER</center>
		</h2>
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
		<c:if test="${!winCondition.lost}">
			<h2 style="color: red;font-size:400%;">
				<center>
					YOU LOST!
				</center>
			</h2>
		</c:if>
		<c:if test="${!winCondition.wonRooms}">
			<center>
				YOU MAY HAVE DEFEATED THE CLOCK, BUT YOU LEFT MUCH BEHIND!
			</center>
		</c:if>
		<c:if test="${!winCondition.bestCase}">
			<center>
				CONGRATULATIONS!  YOU WON!
			</center>
		</c:if>
	
		<form action="${pageContext.servletContext.contextPath}/EndCredits" method="get">
			<center>
			<button type="Submit" formaction="${pageContext.servletContext.contextPath}/titlePage">Press to Continue</button>
			</center>
		</form>
		<h3 style="color: white;font-size:200%;position:fixed;bottom:0;right:0;">
			<center>
				<u>Game Contributors:</u> <br />
				Brady Carbaugh <br />
				Conrad Ogden <br />
				Jordan King <br />
				Robby Weaver <br />
			</center>
		</h3>
	</body>
</html>