<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="show"%>
<!DOCTYPE html>
<html>
<title>HOME</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<style>
body {font-family: "Times New Roman", Georgia, Serif;}
h1, h2, h3, h4, h5, h6 {
  font-family: "Playfair Display";
  letter-spacing: 5px;
}
</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<body>

<div class="w3-top">
  <div class="w3-bar w3-white w3-padding w3-card" style="letter-spacing:4px;">
  <div class="w3-container w3-black">
    <a href="/com.xworks.commonmodule/" class="w3-bar-item w3-button">X-works ODC</a>
    <!-- Right-sided navbar links. Hide them on small screens -->
    <div class="w3-right w3-hide-small">
      <!-- <form:show action="/com.xworks.commonmodule/registerPage" method="post" class="w3-bar-item w3-button"><input type="submit" value="Register"/></form:show>
      <form:show action="/com.xworks.commonmodule/loginPage" method="post" class="w3-bar-item w3-button"><input type="submit" value="Log-In"/></form:show> -->
      
      <a href="/com.xworks.commonmodule/registerPage"
						method="post" class="w3-bar-item w3-button">Register
					</a>
					
					<a href="/com.xworks.commonmodule/loginPage"
						method="post" class="w3-bar-item w3-button">Log-In
					</a>
      </div>
    </div>
  </div>
</div>
<header class="w3-display-container w3-content w3-wide" style="max-width:1600px;min-width:500px" id="home">
  <img class="w3-image" src="/w3images/hamburger.jpg" alt="Hamburger Catering" width="1600" height="800">
  <div class="w3-display-bottomleft w3-padding-large w3-opacity">
    <h1 class="w3-xxlarge">X-works ODC</h1>
  </div>
  
</header>



      <br>
      <br>
      <br>

<h4>${Message}</h4>

	<table>
		<%-- <tr>
			<td>User ID             :</td>
			<td><show:out value="${user.user_id}" /></td>
		</tr> --%>

		<tr>
			<td>Email               :</td>
			<td><show:out value="${user.email }" /></td>
		</tr>

		<%-- <tr>
			<td>Course              :</td>
			<td><show:out value="${user.course }" /></td>
		</tr> --%>
		<tr>
			<td>Generate Password   :</td>
			<td><show:out value="${user.password }" /></td>
		</tr>
	</table>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br><br>
	  <br>
      <br>
      <br>
      <br>
      <br>

<footer class="w3-center w3-light-grey w3-padding-32">
<h3 class="w3-center">About Xworkz</h3>
  <p>© 2020 Copyright:<a href="https://www.facebook.com/xworkzdevelopmentcenter" title="W3.CSS" target="_blank" class="w3-hover-text-green">X-wrokz-ODC.com</a></p>
</footer>


</body>
</html>
