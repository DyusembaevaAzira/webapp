package component

const val styles = """input{
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}"""

const val loading = """
        .ring
{
  position:absolute;
  top:50%;
  left:50%;
  transform:translate(-50%,-50%);
  width:150px;
  height:150px;
  background:transparent;
  border:3px solid #3c3c3c;
  border-radius:50%;
  text-align:center;
  line-height:150px;
  font-family:sans-serif;
  font-size:20px;
  color:#000;
  letter-spacing:4px;
  text-transform:uppercase;
  text-shadow:0 0 10px #fff000;
  box-shadow:0 0 20px rgba(0,0,0,.5);
}
.ring:before
{
  content:'';
  position:absolute;
  top:-3px;
  left:-3px;
  width:100%;
  height:100%;
  border:3px solid transparent;
  border-top:3px solid #fff000;
  border-right:3px solid #fff000;
  border-radius:50%;
  animation:animateC 2s linear infinite;
}
span
{
  display:block;
  position:absolute;
  top:calc(50% - 2px);
  left:50%;
  width:50%;
  height:4px;
  background:transparent;
  transform-origin:left;
  animation:animate 2s linear infinite;
}
span:before
{
  content:'';
  position:absolute;
  width:16px;
  height:16px;
  border-radius:50%;
  background:#fff000;
  top:-6px;
  right:-8px;
  box-shadow:0 0 20px #fff000;
}
@keyframes animateC
{
  0%
  {
    transform:rotate(0deg);
  }
  100%
  {
    transform:rotate(360deg);
  }
}
@keyframes animate
{
  0%
  {
    transform:rotate(45deg);
  }
  100%
  {
    transform:rotate(405deg);
  }
}"""

const val styleDetails = """
    body {
                    height: 100vh;
                        font-family: sans-serif;
                        color: black;
                        line-height: 1.5;
  letter-spacing: 1px;
  margin-top: 2rem;
}
    
    details {
  width: 50%;
  margin: 0 auto ;
  background: #E0E0E0;
  margin-bottom: .5rem;
  box-shadow: 0 .1rem 1rem -.5rem rgba(0,0,0,.4);
  border-radius: 5px;
  overflow: hidden;
}

summary {
  padding: 1rem;
  display: block;
  background: #fff;
  padding-left: 2.2rem;
  position: relative;
  cursor: pointer;
}

summary:before {
  content: '';
  border-width: .4rem;
  border-style: solid;
  border-color: transparent transparent transparent #000;
  position: absolute;
  top: 1.3rem;
  left: 1rem;
  transform: rotate(0);
  transform-origin: .2rem 50%;
  transition: .25s transform ease;
}

details[open] > summary:before {
  transform: rotate(90deg);
}


details summary::-webkit-details-marker {
  display:none;
}

details > ul {
  padding-bottom: 1rem;
  margin-bottom: 0;
}"""


val refStyle = """a {
  outline: none;
  text-decoration: none;
  padding: 2px 1px 0;
}

a:link {
  color: #265301;
}

a:visited {
  color: #437A16;
}

a:focus {
  border-bottom: 1px solid;
  background: #BAE498;
}

a:hover {
  border-bottom: 1px solid;
  background: #CDFEAA;
}

a:active {
  background: #265301;
  color: #CDFEAA;
}
table {
	width: 100%;
	margin-bottom: 20px;
	border: 5px solid #fff;
	border-top: 5px solid #fff;
	border-bottom: 3px solid #fff;
	border-collapse: collapse; 
	outline: 3px solid #ffd300;
	font-size: 15px;
	background: #fff!important;
}
table tr {
	font-weight: bold;
	padding: 7px;
	background: #ffd300;
	border: none;
	text-align: left;
	font-size: 15px;
	border-top: 3px solid #fff;
	border-bottom: 3px solid #ffd300;
}
table td {
	padding: 7px;
	border: none;
	border-top: 3px solid #fff;
	border-bottom: 3px solid #fff;
	font-size: 15px;
}
table tbody tr:nth-child(even){
	background: #f8f8f8!important;
}
"""
