<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
						<input type="text" id="pName" name="pName" value="${strPoints}" ><br>
						<h1>${result}</h1>
						<h1>${Speed}</h1>
						</div>
					</tr>
					
					<tr>
						<div id= "SpeedBox">
						<input type="text" id="pName" name="pName" value="${spdPoints}" ><br>
						</div>
					</tr>
					
					<tr>
						<div id= "ConstitutionBox">
						<input type="text" id="pName" name="pName" value="${conPoints}" ><br>
						</div>
					</tr>
					
					<tr>
						<div id= "charismaBox">
						<input type="text" id="pName" name="pName" value="${chrPoints}" ><br>
						</div>
					</tr>
					
				</form>
			</div>
			
			<div id= "submitButton">
			<input type="submit" id="pName" name="pName" value="Done" onclick="off()">
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
				
				<div class="subsection">Inventory/Utility</div>
				
				<div class="subsection">
					<div class="stats">
						<h1>Stats</h1>
						<td class="statsLabel">Strength:</td>
						<h1>${Speed}</h1>
					</div>
				</div>
				
				<div class="subsection">Timer</div>
				<h1>${result}</h1>
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
