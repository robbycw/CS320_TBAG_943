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

		<!-- Page Header: Has log-out button, display on all pages when logged in. -->
		<c:if test= "${user.created}">
			
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
			
		</c:if>
	
		
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
			<div id="overlayCharacterCreation">
				
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
						<button type="button" id="chrDone" value="Done" onclick="off()" name= "characterSubmit">Done</button>
					</div>
				</form>
			</div >
		</c:if>
		
		<c:if test= "${model.isLevelUp}">
			<div id="overlayLevelUp">
				<h id="levelUpTitle">Level Up!</h>
				<table id="levelUpTextTable">
					<tr>
				    	<td id="levelUpSkillPoints"><p class="output" id="levelUpSkillPoints"></p></td>
				  	</tr>
				  	<tr>
				    	<td id="levelUpSkillPointsText">Points Available</td>
				  	</tr>
				</table>
				
				<table id= "levelUpTable">
					<tr>
						<td text-align: "right">Strength:</td>
					</tr>
					<tr>
						<td text-align: "right">Speed:</td>
						
					</tr>
					<tr>
						<td text-align: "right">Vitality:</td>
					</tr>
					<tr>
						<td text-align: "right">Charisma:</td>
					</tr>
				</table>
				
				<form action="${pageContext.servletContext.contextPath}/game" method="post" onkeydown="return event.key != 'Enter'">
						<table id="levelUpTableTextBox">
							<tr>
								<td text-align: "center"><input type="text" maxlength="2" name="newStrengthStat" value= "0" id="newStrengthStat" style="width:30px;"></td>
							</tr>
							
							<tr>
								<td><input type="text" maxlength="2" name="newSpeedStat" value= "0" id="newSpeedStat" style="width:30px;"></td>
							</tr>
							
							<tr>
								<td><input type="text" maxlength="2" name="newVitalityStat" value= "0" id="newVitalityStat" style="width:30px;"></td>
							</tr>
							
							<tr>
								<td><input type="text" maxlength="2" name="newCharismaStat" value= "0" id="newCharismaStat" style="width:30px;"></td>
							</tr>
						</table>
					
					<table id="plusTable">
						<tr>
							<td><p>+</p></td>
						</tr>
						
						<tr>
							<td><p>+</p></td>
						</tr>
						
						<tr>
							<td><p>+</p></td>
						</tr>
						
						<tr>
							<td><p>+</p></td>
						</tr>
					</table>
					
					<table id="levelUpResultText">
						<tr>
							<td>Strength:</td>
						</tr>
						
						<tr>
							<td>Speed:</td>
						</tr>
						
						<tr>
							<td>Vitality:</td>
						</tr>
						
						<tr>
							<td>Charisma:</td>
						</tr>
					</table>
					
					<table id="levelUpResultNumbers">
						<tr>
							<td class="output" id="updatedStrengthStat">${strengthStat}</td>
						</tr>
						
						<tr>
							<td class="output" id="updatedSpeedStat">${speedStat}</td>
						</tr>
						
						<tr>
							<td class="output" id="updatedVitalityStat">${vitalityStat}</td>
						</tr>
						
						<tr>
							<td class="output" id="updatedCharismaStat">${charismaStat}</td>
						</tr>
					</table>
					
					<p type="output" id="levelUpError"></p>
					<button type="button" id="levelDone" value="Done" onclick="levelUpOff()" name= "levelUP">Done</button>
			
			</form>
			
			</div>
		
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
							You are in a room with three people...	
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
				

				<form action="${pageContext.servletContext.contextPath}/game" method="post" onsubmit=" on();">
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
				
				<div class="subsection"> 
					<div id="stats">
					<table style="text-align: right;">
						<div id="statsNames">
							<tr>
							<td text-align: "right">Strength:</td>
							<td>${strengthStat}</td>
							</tr>
							
						</div>
						<div id="statsNames">
							
							<tr>
							<td text-align: "right">Speed:</td>
							<td>${speedStat}</td>
							</tr>
						</div>
							
						<div id="statsNames">
							<tr>
							<td>Vitality:</td>
							<td>${vitalityStat}</td>
							</tr>
						</div>
							
						<div id="statsNames">
							<tr>
							<td>Charisma:</td>
							<td>${charismaStat}</td>
							</tr>
						</div>
					</table>
					</div>
					<table align="center">
						<div id="statusBox">
							<tr>
							<td align="right">health:</td>
							<td width="30%" align="left">${health}</td>
							<td align="right">Armor:</td>
							<td>${armor}<td>
							</tr>
						</div>
					</table>
					<table align="center">
						<div id="playerNameBox">
							<tr>
							<td>${playerName}</td>
							</tr>
						</div>
						
					</table>
					<table id="xpBar">
						<tr>
							<td><div id="barBack"><div id="barFiller"></div></div></td>
						</tr>
						<tr>
							<td>${xp}</td>
						</tr>
					</table>
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
			
			const submitButton = document.getElementById('chrDone');
			
			const pointLeft = document.getElementById('availStats');
			
			const error = document.getElementById('chrError');
			
			const apparelItem = document.getElementById('apparel');
			const weaponItem = document.getElementById('weapon');
			const toolItem = document.getElementById('tool');
			const miscItem = document.getElementById('misc');
			
			const console = document.getElementById('user');
			
			const itemNum = 0;
		
			error.innerHTML = "";
				
			pointLeft.innerHTML = 21;
				
			strengthStatBox.addEventListener('input', calculatePoints);
			speedStatBox.addEventListener('input', calculatePoints);
			vitalityStatBox.addEventListener('input', calculatePoints);
			charismaStatBox.addEventListener('input', calculatePoints);
			
			
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
					document.getElementById("overlayCharacterCreation").style.display = "none";
				}
			}
			function on(){
				document.getElementById("overlayLevelUp").style.display = "block";
			}
		</script>
		
		<script>
			const strengthLevelUp = document.getElementById('newStrengthStat')
			const speedLevelUp = document.getElementById('newSpeedStat')
			const vitailityLevelUp = document.getElementById('newVitalityStat')
			const charismaLevelUp = document.getElementById('newCharismaStat')
			
			
			const levelUpPointsLeft = document.getElementById('levelUpSkillPoints');
			
			const levelSubmitButton = document.getElementById('levelDone');
			
			const levelUpError = document.getElementById('levelUpError');
			
			levelUpPointsLeft.innerHTML = 5;
			
			levelUpError.innerHTML = "asfsafa";
			
			newStrengthStat.addEventListener('input', calculateLevelUp);
			newSpeedStat.addEventListener('input', calculateLevelUp);
			newVitalityStat.addEventListener('input', calculateLevelUp);
			newCharismaStat.addEventListener('input', calculateLevelUp);
			
			
			function calculateLevelUp(){
				levelUpPointsLeft.innerHTML = 5;
				levelUpPointsLeft.innerHTML = levelUpPointsLeft.innerHTML - (strengthLevelUp.value);
				levelUpPointsLeft.innerHTML = levelUpPointsLeft.innerHTML - (speedLevelUp.value);
				levelUpPointsLeft.innerHTML = levelUpPointsLeft.innerHTML - (vitailityLevelUp.value);
				levelUpPointsLeft.innerHTML = levelUpPointsLeft.innerHTML - (charismaLevelUp.value);
				
				if(levelUpPointsLeft.innerHTML < 0){
					document.getElementById("levelUpSkillPoints").style.color = "red";
				}else{
					document.getElementById("levelUpSkillPoints").style.color = "blue";
				}
			}
	
			function updateXp(){
				document.getElementById("statsNames").style.color = "red";
			}
			function levelUpOff(){
				if(levelUpPointsLeft.innerHTML == 5){
					levelUpError.innerHTML = "Error, points need to be allocated";
				}else if(levelUpPointsLeft.innerHTML < 0 || levelUpPointsLeft.innerHTML > 0){
					levelUpError.innerHTML = "Error, points are allocated improperly";
				}else{
					document.getElementById("overlayLevelUp").style.display = "none";
					levelSubmitButton.type = "submit";
				}
			}
		</script>
	</body>
</html>
