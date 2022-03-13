<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Combat</title>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: right;
		}
		
		#options{
		margin-top:300px;
		}
		
		#fight {
  		margin-top: 0px;
        margin-right:0px;
  		font-size: 150%;
        float:left;
        color: red;
		}
		
		#item {
        margin-top:0px;
        margin-right:0px;
  		color: black;
  		font-size: 150%;
        float:left;
		}
		
		#run {
        margin-top:0px;
  		color: blue;
  		font-size: 150%;
		}
		
		
		
		</style>
	</head>

	<body>
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
		
		<div id="options">
		<div id="fight">
		<form action="${pageContext.servletContext.contextPath}/combat" method="post">
			<td class="label">Fight</td>
			<td><input type="image" src="https://cdn3.iconfinder.com/data/icons/glypho-travel/64/history-swords-crossed-512.png" 
					style="width:40px;height:40px;" name="fight" value="Fight">
		</form>
		</div>
		
		
		<div id="item">
		<form action="${pageContext.servletContext.contextPath}/combat" method="post">
			<td class="label">Item</td>
			<td><input type="image" src="https://cdn4.iconfinder.com/data/icons/kitchen-vol-3/100/139-512.png" 
					style="width:40px;height:40px"name="item" value="Item">
		</form>
		</div>
		
		
		<div id="run">
		<form action="${pageContext.servletContext.contextPath}/game" method="get">
			<td class="label">Run</td>
			<td><input type="submit" name="run" value="Run">
		</form>
		</div>
		</div>
		
	</body>
</html>