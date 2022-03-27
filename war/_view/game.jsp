<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import = "edu.ycp.cs320.tbag_943.classes.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<html>
	<head>
		<title>9:43</title>

		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Encode+Sans+SC&display=swap" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css2?family=Encode+Sans&family=Encode+Sans+SC&display=swap" rel="stylesheet">
		<style type="text/css">
			<%@include file="gamestyle.css" %> 
		</style>
		
	</head>

	<body>
		<c:if test= "${model.playerNotCreated}">
			<div id="overlay">
				<div id="charCreationTxt">
					<p>Name:</p>
					<p>Strength:</p>
					<p>Speed:</p>
					<p>Vitality:</p>
					<p>Charisma:</p>
				</div>
			
			
			
		
				<form action="${pageContext.servletContext.contextPath}/game" method="post">
					<div id="charCreationBoxes">
						
							<tr>
								<div id= "nameBox">
									<input type="text" name="playerName">
									
								</div>
							</tr>
							
							<tr>
								<div id= "strengthBox">
									<input type="text" maxlength="2" name="strengthStat"><br>
								
								</div>
							</tr>
							
							<tr>
								<div id= "SpeedBox">
									<input type="text" maxlength="2" name="speedStat"><br>
								</div>
							</tr>
							
							<tr>
								<div id= "ConstitutionBox">
									<input type="text" maxlength="2" name="vitalityStat"><br>
								</div>
							</tr>
							
							<tr>
								<div id= "charismaBox">
									<input type="text" maxlength="2" name="charismaStat"><br>
								</div>
							</tr>
							
							
					</div>
					
					<div id= "submitButton">
						<input type="submit" id="chrDone" value="Done" onclick="off()" name= "characterSubmit">
					</div>
					
				</form>
			</div >
		</c:if>
		
		
		<div id="entire"><div id="row">
		
		
			<div class="column">
				Map goes here. 
			</div>
		
		
			<div class="column"> Console goes here.
				
				<div id="output" style="height: 200px; overflow: auto">
					
					<table>

						<c:forEach items="${model.outputLog}" var="str">

							<tr>
								<td>${str}</td>
							</tr>

						</c:forEach>


					</table>
					<p>
					</p>
				</div>

				<!-- This script was found at https://www.codegrepper.com/code-examples/javascript/javascript+scroll+to+bottom+of+table -->
				<script>
					var myDiv = document.getElementById("output");
					myDiv.scrollTop = myDiv.scrollHeight; 
				</script>

				<form action="${pageContext.servletContext.contextPath}/game" method="post">
					<input type="text" id="user" name="user"><br>
					<input type="submit" value="Enter" id="enterButton"><br>
				</form>

				<form action="${pageContext.servletContext.contextPath}/game" method="post">
					<input type="submit" name="title" value="Title Page">
					<input type="submit" name="credits" value="Credits">
					<input type="submit" name="combat" value="Combat">
				</form>

			</div>
			
			<div class="column">
				
				<div class="subsection">Inventory/Utility
				</div>
				
				<div class="subsection"> <br>
					<div id="statNameBox">
						<div id="statsNames">
						<td>Strength:</td>
						</div>
						
						<div id="statsNames">
						<td>Speed:</td>
						</div>
						
						<div id="statsNames">
						<td>Vitality:</td>
						</div>
						
						<div id="statsNames">
						<td>Charisma:</td>
						</div>
						
					</div>
					<div id="statsBox">
						<h1>${strengthStat}</h1>	
						<h1>${speedStat}</h1>	
						<h1 style="margin-top: -1px">${vitalityStat}</h1>
						<h1 style="margin-top: -1px">${charismaStat}</h1>
					</div>
					
					<div id="healthBox">
						<h1>health:</h1>
					</div>
					
					<div id= "playerHealth">
						<h1>${health}</h>
					</div>
					
					<div id="playerNameBox">
					<h1 >${playerName}</h1>
					</div>
				</div>
				
				<div class="subsection">Timer</div>
			</div>
		
		</div></div>
		<script>
			function on() {
			  	document.getElementById("overlay").style.display = "block";
			}
	
			function off() {
				document.getElementById("overlay").style.display = "none";
			}
		</script>
		
	</body>
</html>
