<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import = "edu.ycp.cs320.tbag_943.classes.*" %>
<%@ page import ="java.util.*" %>

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
	
		<%-- Based this script off of a tutorial from W3Schools. Link: https://www.w3schools.com/howto/howto_js_countdown.asp --%>
		<script> 
		// This script is for the game's countdown timer. 
		// Get the current time stored in the Game's timer. 
		var start = ${model.timer.time};
		
		// Use the setInterval to call a function that decrements the timer by the decrement every second. 
		var x = setInterval(function(){
			// Decrease seconds by 1
			start = start - 1; 

			// Calculate hours, minutes, and seconds remaining. 
			var h = Math.floor(start / 3600); 
			var m = Math.floor((start - (h * 3600)) / 60); 
			var s = (start - (h * 3600)) - (m * 60);

			// Store the results into the paragraph element for the timer. 
			document.getElementById("time").innerHTML = String(h) + "h " + String(m) + "m " + String(s) + "s"; 

			// Timer will be tracked independently on Servlet. 
			document.getElementById("timeLeft2").value = String(start);  
		}, 1000);  

		</script>

		<c:if test= "${model.playerNotCreated}">
			<div id="overlay">
				
				<div id="charCreationTxt">
					<p>Name:</p>
					<p>Strength:</p>
					<p>Speed:</p>
					<p>Vitality:</p>
					<p>Charisma:</p>
				</div>
			
			
			
		
				<form action="${pageContext.servletContext.contextPath}/game" method="post" onkeydown="return event.key != 'Enter'">
					
					<div id="charCreationBoxes">
						
							<tr>
								<div id= "nameBox">
									<input type="text" name="playerName" value="--" id="pNameBox">
									
								</div>
							</tr>
							
							<tr>
								<div id= "strengthBox">
									<input type="text" maxlength="2" name="strengthStat" value= "0" id="strengthStatBox"><br>
								
								</div>
							</tr>
							
							<tr>
								<div id= "SpeedBox">
									<input type="text" maxlength="2" name="speedStat" value= "0" id="speedStatBox"><br>
								</div>
							</tr>
							
							<tr>
								<div id= "VitalityBox">
									<input type="text" maxlength="2" name="vitalityStat" value="0" id="vitalityStatBox"><br>
								</div>
							</tr>
							
							<tr>
								<div id= "charismaBox">
									<input type="text" maxlength="2" name="charismaStat" value="0" id="charismaStatBox"><br>
								</div>
							</tr>
							
							
					</div>
				
					<div id= "SkillPoints">
						<p class="output" id="availStats"></p>
					</div>
					<div id= "SkillPointsText">
						<p>Points Available</p>
					</div>
					<div id= "StartingItems">
						
						<div id="PickWeapons">
							 <label for="weapons">Weapon:</label>
							 <select name="weapons" id="weapon" >
							 	<option value="--">--</option>
							    <option value="revolver">Revolver</option>
							    <option value="baton">Baton</option>
							    <option value="brass knuckles">Brass Knuckles</option>
							    <option value="hard salami">Hard Salami</option>
							 </select>
						 </div>
						 
						 <div id="PickApparel">
							 <label for="apparel">Apparel:</label>
							 <select name="apparel" id="apparel">
							 	<option value="--">--</option>
							    <option value="Sunday suit">Sunday Suit</option>
							    <option value="old police uniform">Old Police Uniform</option>
							    <option value="dectective's duster">Detective's Duster</option>
							    <option value="birthday suit">Birthday Suit</option>
							 </select>
						 </div>
						  
						  <div id="PickTool">
							  <label for="tool">Tool:</label>
							  <select name="tool" id="tool">
								   <option value="--">--</option>
								   <option value="wire cutters">Wire Cutters</option>
								   <option value="axe">Rusty Axe</option>
								   <option value="crowbar">Crowbar</option>
								   <option value="case file">Case File</option>
							  </select>
						  </div>
						  
						  <div id="PickMisc">
							 <label for="misc">Misc:</label>
							 <select name="misc" id="misc" onclick="itemSelected()">
							 	<option value="--">--</option>
							    <option value="box of cigars">Box of Cigars</option>
							    <option value="flask of whiskey">Flask of Whiskey</option>
							    <option value="pill bottle">Pill Bottle</option>
							    <option value="teddy bear">Teddy Bear</option>
							 </select>
						 </div>
					</div>
					<div id= "CharacterCreationError">
						<p class="output" id="chrError"></p>
					</div>
					<div id= "submitButton">
						<button type="button" id="chrDone" value="Done" onclick="off()" name= "characterSubmit">Done</button>
						
					</div>
					
					
				</form>
			</div >
		</c:if>
		
		
		<div id="entire"><div id="row">
		
		
			<div class="column">
				Map goes here. 

				<%-- These divs below print out the map, namely the rooms connected to .--%> 
					
				 
				<div style="background-color:gray; width: 95%; height: 25%;"> <%-- Row 1--%>
				
					<div style="background-color:${nwc}; width: 33.3%; height: 100%; float: left;">${nwr}</div><%-- Northwest --%>
					<div style="background-color:${northc}; width: 33.3%; height: 100%; float: left;">${northr}</div><%-- North --%>
					<div style="background-color:${nec}; width: 33.3%; height: 100%; float: left;">${ner}</div><%-- Northeast --%>
				
				</div>
				<div style="background-color:gray; width: 95%; height: 25%;"> <%-- Row 2--%>
				
					<div style="background-color:${westc}; width: 33.3%; height: 100%; float: left;">${westr}</div><%-- West --%>
					<div style="background-color:blue; width: 33.3%; height: 100%; float: left;">${currentr}</div><%-- Current --%>
					<div style="background-color:${eastc}; width: 33.3%; height: 100%; float: left;">${eastr}</div><%-- East --%>
				
				</div>
				<div style="background-color:gray; width: 95%; height: 25%;"> <%-- Row 3--%>
				
					<div style="background-color:${swc}; width: 33.3%; height: 100%; float: left;">${swr}</div><%-- Southwest --%>
					<div style="background-color:${southc}; width: 33.3%; height: 100%; float: left;">${southr}</div><%-- South --%>
					<div style="background-color:${sec}; width: 33.3%; height: 100%; float: left;">${ser}</div><%-- Southeast --%>
				
				</div>
			</div>
		
		
			<div class="column"> Console
				
				<div id="output" style="height: 200px; overflow: auto">
			
					<table align=center>
						<center>
							You Enter Tired...
						</center>
						<center>
							The clock reads 9:43...	
						</center>
						<center>
							Many murders have occured here...	
						</center>
						<center>
							You have two hours to investigate those murders...	
						</center>
						<center>
							Time Starts Now!	
						</center>

						<div align=center>
							${description}	
						</div>

						<c:forEach items="${model.outputLog}" var="str" >
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
					<input type="hidden" id="timeLeft2" name="t" value="">
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
					
					<div id="armorBox">
						<h1>Armor:</h1>
					</div>
					
					<div id="armorStat">
						<h1>${armor}</h1>
					</div>
					
				</div>
				
				<div class="subsection">Timer<br>
					
					<p id="time"></p>

				</div>
			</div>
		
		</div></div>
		<script>
			const characterName = document.getElementById('pNameBox');
			const strengthPoints = document.getElementById('strengthStatBox');
			const speedPoints = document.getElementById('speedStatBox');
			const vitalityPoints = document.getElementById('vitalityStatBox');
			const charismaPoints = document.getElementById('charismaStatBox');
			
			const pointLeft = document.getElementById('availStats');
			
			const submitButton = document.getElementById('chrDone');
			
			const error = document.getElementById('chrError');
			
			const apparelItem = document.getElementById('apparel');
			const weaponItem = document.getElementById('weapon');
			const toolItem = document.getElementById('tool');
			const miscItem = document.getElementById('misc');
			
			const itemNum = 0;
		
			error.innerHTML = "";
			pointLeft.innerHTML = 21;
			
			function calculatePoints(){
				pointLeft.innerHTML = 21;
				pointLeft.innerHTML = pointLeft.innerHTML - (strengthPoints.value);
				pointLeft.innerHTML = pointLeft.innerHTML - (speedPoints.value);
				pointLeft.innerHTML = pointLeft.innerHTML - (vitalityStatBox.value);
				pointLeft.innerHTML = pointLeft.innerHTML - (charismaStatBox.value);
				
				if(pointLeft.innerHTML < 0){
					document.getElementById("SkillPoints").style.color = "red";
				}else{
					document.getElementById("SkillPoints").style.color = "blue";
				}
			}
		
			strengthStatBox.addEventListener('input', calculatePoints);
			speedStatBox.addEventListener('input', calculatePoints);
			vitalityStatBox.addEventListener('input', calculatePoints);
			charismaStatBox.addEventListener('input', calculatePoints);
			
	
			function off() {
				if(pointLeft.innerHTML == 21){
					error.innerHTML = "Error, points need to be allocated";
				}else if(pointLeft.innerHTML < 0 || pointLeft.innerHTML > 0){
					error.innerHTML = "Error, points are allocated improperly";
				}else if(characterName.value == "--"){
					error.innerHTML = "Error, name has not been typed in"
				}else if(miscItem.value == "--" || toolItem.value == "--" || weaponItem.value == "--" || apparelItem.value == "--"){
					error.innerHTML = "Error, all items have not been properly chosen";
				}else{
					error.innerHTML = "done";
					submitButton.type = "submit";
					document.getElementById("overlay").style.display = "none";
				}
			}
		</script>
		
	</body>
</html>
