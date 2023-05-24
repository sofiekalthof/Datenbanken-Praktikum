<html>
<head><title>new_enroll</title>
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
   text-align:left;
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
		<h1>${coursename}  </h1>
		</div>
	   
		<div id="site">
			<#if einschreibeschluessel == "" >
			<form name="enroll" action="view_course?kid=${kid}&b=1&a=0" method="post">
					<input style="color:MediumSeaGreen;" type="submit" value="Einschreiben"/>
					<input type="hidden" name="coursename" value="${coursename}">
					<input type="hidden" name="kid" value="${kid}">
			</form>
			<#else>
			<form name="enroll" action="view_course?kid=${kid}&b=1&a=0" method="post">
					Einschreibeschlüssel: <input type ="text" name="schluessel" /> <br/> <br/>
					<input style="color:MediumSeaGreen;" type="submit" value="Einschreiben"/>
					<input type="hidden" name="coursename" value="${coursename}">
					<input type="hidden" name="kid" value="${kid}">
			</form>
			</#if>
			
			<#if fehlerSchluessel==1>
				<p style="color:Tomato;" >Falscher Einschreibeschlüssel. </p>
			</#if>
			
			<#if fehlerPlaetze==1>
				<p style="color:Tomato;" >Kein freier Platz mehr </p>
			</#if>
			<a href="onlineLearner"><button style="color:DodgerBlue;"> Zurück zur Startseite </button> </a>
		
		</div>
	</div>
</body>
</html>