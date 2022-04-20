<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html style="height: 100%;">
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
		
	<body style="height: 100%; display: flex; justify-content: center; align-items: center; background-color: black;">

		<!-- Login Overlay -->
		<c:if test= "${! user.created}">
			<div class="overlay"> 
				
				<form action="${pageContext.servletContext.contextPath}/titlePage" method="post" style="width: 100%;">
					
					<div id="loginBoxes">
						<table>
							<tr>
								
								<h1 style="margin: 0px;">Login to 9:43</h1>
								
							</tr>

							<tr>
								<td>
									<div class="loginText">
										<p>Username:</p>
										
									</div>
								</td>

								<td>
									<div id= "usernameBox">
										<input type="text" name="username" maxlength="30">
										
									</div>
								</td>
								<td>
									<div id= "loginError">
										<p>${loginErr}</p>
									</div>
								</td>
							</tr>
							
							<tr>
								<td>
									<div class="loginText">
										<p>Password:</p>
									</div>
								</td>

								<td>
									<div id= "passwordBox">
										<input type="password" maxlength="30" name="password"><br>
									
									</div>
								</td>
								
							</tr>

							<tr>
								<td>
									<div class= "submitButton">
										<button type="submit" value="Login" onclick="off()" name= "loginSubmit">Login</button>
										
									</div>
								</td>
								<td>
									<div class= "submitButton">
										<button type="submit" value="Create Account" onclick="off()" name= "createAccount">Create an Account</button>
										
									</div>
								</td>
							</tr>

						</table>	
					</div>
				
				</form>
			</div >
		</c:if>

		<!-- Create Account Overlay-->
		<!-- Login Overlay -->
		<c:if test= "${makeNewAccount}">
			<div class="overlay" style="z-index: 3;"> 
				
				<form action="${pageContext.servletContext.contextPath}/titlePage" method="post" style="width: 100%;">
					
					<div id="loginBoxes">
						<table>
						<tr>
							
							<h1 style="margin: 0px;">Create an Account</h1>
							
						</tr>
							<tr>
								<td>
									<div class="loginText">
										<p>Username:</p>
										
									</div>
								</td>

								<td>
									<div id= "usernameBox">
										<input type="text" name="username" maxlength="30">
										
									</div>
								</td>

								<td>
									<div id= "loginError">
										<p>${loginErr}</p>
									</div>
								</td>
							</tr>
							
							<tr>
								<td>
									<div class="loginText">
										<p>Password:</p>
									</div>
								</td>

								<td>
									<div id= "passwordBox">
										<input type="password" maxlength="30" name="password"><br>
									
									</div>
								</td>
								
							</tr>

							<tr>
								<td>
									<div class= "submitButton">
										<button type="submit" value="Create Account" onclick="off()" name= "attemptCreateAccount">Create an Account</button>
										
									</div>
								</td>

								<td>
									<div class= "submitButton">
										<button type="submit" value="Cancel" onclick="off()" name= "createCancel">Cancel</button>
										
									</div>
								</td>
							</tr>
							
						</table>	
					</div>

				</form>
			</div >
		</c:if>

		<!-- Title Page Buttons --> 

		<div id="entire">
			<table> 
				<tr>
					<td>
					<div id="gameTitle">
					<h1>9:43</h1>		
					</td>
				</tr> 

				<tr>
					<td>
					<img src="https://i.imgur.com/tqGAqsN.gif" alt="gif" height="200px" width="200px" style="position: relative; top: 0; left: 0; display:block;"/>
					</td>
				</tr> 

				<tr>
					<td>
					<div class="buttons" width=100%>
						<form action="${pageContext.servletContext.contextPath}/titlePage" method="post">
							<input type="submit" name="game" value="Play Game!">
						</form>
					</div>
					</td>
				</tr> 

				<tr>
					<td>
					<div class="buttons" width=100%>
						<form action="${pageContext.servletContext.contextPath}/titlePage" method="post">
							<input type="submit" name="options" value="Options">
						</form>
					</div>
					</td>
				</tr> 

				<tr>
					<td>
					<div class="buttons" width=100%>
						<form action="${pageContext.servletContext.contextPath}/titlePage" method="post">
							<input type="submit" name="credits" value="Credits">
						</form>
					</div>
					</td>
				</tr> 

			</table> 
		</div>
		
	</body>
</html>