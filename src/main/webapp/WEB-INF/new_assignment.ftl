<html>
<head><title>new_assignment</title>
<style type="text/css">
* {
   margin:0;
   padding:0;
}

body{
   text-align:center;
   background: #efe4bf none repeat scroll 0 0;
}

#wrapper{
   width:960px;
   margin:0 auto;
   text-align:middle;
   background-color: #fff;
   border-radius: 0 0 10px 10px;
   padding: 20px;
   box-shadow: 1px -2px 14px rgba(0, 0, 0, 0.4);
}

#header{
 color: #fff;
 background-color: #2c5b9c;
 height: 3.5em;
 padding: 1em 0em 1em 1em;
 
}

#site{
    background-color: #fff;
    padding: 20px 0px 0px 0px;
}
.centerBlock{
	margin:0 auto;
}
</style>

<body>
	<div id="wrapper">
		<div id="header">
		<h1></h1>
		</div>
	   
		<div id="site">
		Kurs: ${coursename} <br> <br>
		Aufgabe: ${aufgabename} <br> <br>
		Beschreibung: ${beschreibung}
		<form name="assignment" action="view_course?kid=${kid}&b=1&a=1" method="post">
			Abgabetext: <textarea name="abgabe" rows="4" cols="30"> </textarea> <br/> <br/>
			<input type="hidden" name="aufgabe" value="${aufgabename}">
			<input type="hidden" name="beschreibung" value="${beschreibung}">
			<input style="color:MediumSeaGreen;" type="submit" value="Einreichen" />
		</form> 
		
		<#if fehlerAbgabe==1>
			<p style="color:Tomato;" >Abgabe bereits vorhanden </p>
		</#if>
		<a href="view_course?kid=${kid}&b=1"><button style="color:DodgerBlue;"> Zurück zum Kurs </button> </a>
		
		</div>
	</div>
</body>
</html>