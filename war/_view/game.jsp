<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import = "edu.ycp.cs320.tbag_943.classes.*" %>
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
		<div id="overlay">
			<div id="charCreationTxt">
			<p>Name:</p>
			<p>Strength:</p>
			<p>Speed:</p>
			<p>Constitution:</p>
			<p>Charisma:</p>
			</div >
			
			<div id="charCreationBoxes">
				<form action="${pageContext.servletContext.contextPath}/game" method="post">
					<tr>
						<div id= "nameBox">
							<input type="text" id="pName" name="pName" value="${name}">
							
						</div>
					</tr>
					
					<tr>
						<div id= "strengthBox">
						<input type="text" id="strPoints" maxlength="2"><br>
						
						</div>
					</tr>
					
					<tr>
						<div id= "SpeedBox">
						<input type="text" id="spdPoints" maxlength="2"><br>
						</div>
					</tr>
					
					<tr>
						<div id= "ConstitutionBox">
						<input type="text" id="conPoints" maxlength="2"><br>
						</div>
					</tr>
					
					<tr>
						<div id= "charismaBox">
						<input type="text" id="chrPoints" maxlength="2"><br>
						</div>
					</tr>
					
					
				</form>
			</div>
			
			<div id= "submitButton">
			<input type="submit" id="chrDone" value="Done" onclick="translateStats()">
			</div>
		</div>
		
		
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

				</div>

				<!-- This script was found at https://www.codegrepper.com/code-examples/javascript/javascript+scroll+to+bottom+of+table -->
				<script>
					var myDiv = document.getElementById("output");
					myDiv.scrollTop = myDiv.scrollHeight; 
				</script>

				<form action="${pageContext.servletContext.contextPath}/game" method="post">
					<input type="text" id="user" name="user"><br>
					<input type="submit" value="Enter"><br>
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
				
				<div class="subsection"> Stats
					<div id="statNameBox">
						<div id="statsNames">
						<td>Strength:</td>
						</div>
						
						<div id="statsNames">
						<td>Speed:</td>
						</div>
						
						<div id="statsNames">
						<td>Charisma:</td>
						</div>
						
					</div>
					<div id="statsBox">
						<h1 class="output" id="strStat"></h1>	
						<h1 class="output" id="spdStat"></h1>	
						<h1 class="output" id="chrStat" style="margin-top: -1px"></h1>
					</div>
				</div>
				
				<div class="subsection">Timer</div>
			</div>
		
		</div></div>
		<script>
		const playerName = document.getElementById('pName');
		const strengthPoints = document.getElementById('strPoints');
		const speedPoints = document.getElementById('spdPoints');
		const charismaPoints = document.getElementById('chrPoints');
		
		const strengthStat = document.getElementById('strStat');
		const speedStat = document.getElementById('spdStat');
		const charismaStat = document.getElementById('chrStat');
		
		const btn1 = document.getElementById('chrDone');
		
		function translateStats(){
			strengthStat.innerHTML = strengthPoints.value;
			speedStat.innerHTML = speedPoints.value;
			charismaStat.innerHTML = charismaPoints.value;
			off();	
		}
		
		
		function on() {
		  	document.getElementById("overlay").style.display = "block";
		}

		function off() {
			document.getElementById("overlay").style.display = "none";
		}
		
		</script>
		
	</body>
</html>
