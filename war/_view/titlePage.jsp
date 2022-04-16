<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html style="height: 96%">
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
	</head>
		
	<body style="height: 100%">

		<c:if test= "${! user.created}">
			<div id="overlay">
				
				<div id="loginText">
					<p>Username:</p>
					<p>Password:</p>
				</div>
			
			
			
		
				<form action="${pageContext.servletContext.contextPath}/titlePage" method="post">
					
					<div id="loginBoxes">
						
							<tr>
								<div id= "usernameBox">
									<input type="text" name="username" maxlength="30">
									
								</div>
							</tr>
							
							<tr>
								<div id= "passwordBox">
									<input type="password" maxlength="30" name="password"><br>
								
								</div>
							</tr>
							
							
					</div>
					
					<div id= "loginError">
						<p class="output" id="loginError">${loginErr}</p>
					</div>
					<div id= "submitButton">
						<button type="submit" value="Login" onclick="off()" name= "loginSubmit">Login</button>
						
					</div>
					
					
				</form>
			</div >
		</c:if>

	<!-- Title Page Buttons --> 

		<div id="entire">
			<div id="gameTitle">
				<h1>9:43</h1>
				<img src="https://i.imgur.com/tqGAqsN.gif" alt="gif" height="200" width="200"/>
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
</html>