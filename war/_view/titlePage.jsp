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
		
	<body style="height: 99%; width: 99%; margin: 0px;">

		<!-- Page Header: Has log-out button, display on all pages when logged in. -->
		<c:if test= "${user.created}">
		<div style="width: 100%; height: 10%; align-items: top; display: flex; position: absolute; z-index: 2;">	
			<div id="header">
			<form action="${pageContext.servletContext.contextPath}/titlePage" method="post" style="width: 100%;">
				<table style="width:100%">
					<tr>
						<td style="width:15%">${user.username}</td>
						<td>
							<div class= "submitButton" style="margin-top: 0px">
								<button type="submit" value="Log Out" name= "logOut">Log Out</button>
							</div>
						</td>
						<td>
							<div class= "submitButton" style="margin-top: 0px">
								<button type="submit" value="Title Page" name= "titlePage">Title Page</button>
							</div>
						</td>
					</tr>
				</table>
			</form>
			</div>
		</div>
		</c:if>
	<div style="height: 100%; width: 100%; display: flex; background-color: black; justify-content: center; align-items: center; position: absolute; z-index: 1;">
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

		<!-- Select Game Overlay -->
		<c:if test= "${playGameClicked}">
			<div class="overlay"> 
				
				<form action="${pageContext.servletContext.contextPath}/titlePage" method="post" style="width: 100%; height: 100%;">
					
					<div id="loginBoxes" style="overflow: auto; height: 80%;">


						<table>
							<tr>
								
								<h1 style="margin: 0px;">Select Game</h1>
								
							</tr>

							<tr>
								<td width="20%">Game ID</td> <!-- Game ID -->
								<td width="20%">Player Name</td> <!-- Player Name -->
								<td width="20%">Time Remaining</td> <!-- Time Remaining -->	
							</tr>

							<!-- List all of the user's games. -->
							<c:forEach items="${user.gameList}" var="g" >
								
								<tr>
									<td width="20%">${g.idString}</td> <!-- Game ID -->
									<td width="20%">${g.player.name}</td> <!-- Player Name -->
									<td width="20%">${g.timer.time}</td> <!-- Time Remaining -->
									<td width="20%"> <!-- Button to Select this Game -->
										<div class= "submitButton">
											<button type="submit" value="Load Game" onclick="off()" name= "${g.idString}">Load Game</button>
												
										</div>
									</td> 
								</tr>

							</c:forEach>

							<tr>
								<td width="20%">
									<div class= "submitButton">
										<button type="submit" value="New Game" onclick="off()" name= "newGame">New Game</button>
										
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
	</div>
	</body>
</html>