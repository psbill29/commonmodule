<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<title>HOME</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<style>
body {
	font-family: "Times New Roman", Georgia, Serif;
}

h1, h2, h3, h4, h5, h6 {
	font-family: "Playfair Display";
	letter-spacing: 5px;
	
}
ul:after{display:block;height:21px; background:blue;margin-top:20px;}
</style>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<body>

	<div class="w3-top">
		<div class="w3-bar w3-white w3-padding w3-card"
			style="letter-spacing: 4px;">
			<div class="w3-container w3-black">
				<a href="#home" class="w3-bar-item w3-button">Description Page</a>
				<!-- Right-sided navbar links. Hide them on small screens -->
				<div class="w3-right w3-hide-small">
					<%-- <form:form action="/com.xworks.commonmodule/registerPage"
						method="post" class="w3-bar-item w3-button">
						<input type="submit" value="Register">
					</form:form> --%>
					
					
					 <a href="/com.xworks.commonmodule/registerPage"
						method="post" class="w3-bar-item w3-button">Register
					</a>
					
					<a href="/com.xworks.commonmodule/loginPage"
						method="post" class="w3-bar-item w3-button">Log-In
					</a>
					<%-- <form:form action="/com.xworks.commonmodule/loginPage"
						method="post" class="w3-bar-item w3-button">
						<input type="submit" value="Log-In">
					</form:form> --%>
				</div>
			</div>
		</div>
	</div>

	</header>
	<br><br><br>

	<h3 style="text-align:center;"><u style="color:red;">${Message}</u></h3><br>

	<div class="w3-container" style="padding: 32px">

		<img alt="Xworkz logo" src="/com.xworks.commonmodule/xwrokz.jpg" width=18% style="margin:0px 50px">
		<h3 style="margin:0px 70px"><u>X-workz ODC</u></h3>
		

		<p><u>Software training institute in Bengaluru, Karnataka</u></p>
		<br>

		<ul class="w3-leftbar w3-theme-border" style="list-style: none">
			<h5:after><ul><u><font size="+2">X-workZ Omkar Development Center</font></u></ul></h5:after><h5> Provides free Java Training
				. Advanced Java Training classes , SQL, Web Technologies ( HTML, CSS
				and JavaScript), Frameworks ( Springs and Hibernate Training ) and
				Angular Training with real time enterprise application development
				training will be provided.</h5>
			<br>
			<br>
			<h6>Address Basement #17/10/28, Dr.Rajkumar Road, 3rd Stage, 4th
				Block, Prakash Nagar, Prakash Nagar, Bangalore, India - 560021</h6>
		</ul>
		
		
		

		<footer class="w3-center w3-light-grey w3-padding-32">
			<h3 class="w3-center">About Xworkz</h3>
			<p>
				© 2020 Copyright:<a
					href="https://www.facebook.com/xworkzdevelopmentcenter"
					title="W3.CSS" target="_blank" class="w3-hover-text-green">X-wrokz-ODC.com</a>
			</p>
		</footer>
</body>
</html>
