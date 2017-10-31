<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>University JSP and Servlet</title>
</head>
<body>
    <h1>University Units</h1>
    <h3>Choose University Category to display: </h3>
    <form action="courseServlet" method="post">	    
	    <input type="submit" name ="unit" value="Course" />
	    <input type="submit" name ="unit" value="Group" />
	    <input type="submit" name ="unit" value="Student" />
	    <input type="submit" name ="unit" value="Professor" />
	    <input type="submit" name ="unit" value="Room" />
    </form>
</body>
</html>