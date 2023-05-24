<html>
<head><title>view_course</title>
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

th, td {
  padding: 15px;
}

table {
  border-spacing: 5px;
  text-align: middle;
  margin-left:auto;
  margin-right:auto;
}


</style>


<body>
	<div id="wrapper">
		<div id="header">
		<h1> Informationen </h1>
		</div>
		<div id="site">
		<h2>${coursename}</h2> <br>
		<#list course as c>
		<p>
			<h4>Ersteller:${c.ersteller}</h4><br>
			<h4>Beschreibung: ${c.beschreibungstext}</h4> <br>
			<h4>Anz. freie Plätze:${c.freiePlaetze}</h4><br> <br>
			<#if c.ersteller == "DummyUser">
				<form name="delete" action="onlineLearner" method="post">
					<input style="color:Tomato;" type="submit" value="Kurs löschen"/>
					<input type="hidden" name="coursename" value="${coursename}">
					<input type="hidden" name="kid" value="${kid}">
				</form>
			</#if>
		</p>
		</#list>
		<#if bnummer == "1" >
			<h2>Liste der Aufgaben</h2>
			<table class="datatable" style="width100%">
				<tr>
					<th>Aufgaben</th> <th>Meine Abgabe</th> <th>Bewertungsnote</th>
				<#list aufgaben as a>
				<tr>
					<td><a href="new_assignment?kid=${kid}&aufgabe=${a.name}">${a.name}</td> <td>${a.abgabetext}</td> <td>${a.note}</td>
				</tr>
				</#list>
			</table>
		<#else>
				<a href="new_enroll?kid=${kid}"><button style="color:MediumSeaGreen;"> Einschreiben </button> </a>	<br>
		</#if>
		<a href="onlineLearner"><button style="color:DodgerBlue;"> Zurück zur Startseite </button> </a>
		</div>
	</div>
</body>
</html>