<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
	<tr>
		<td>
		<%=(first>0)?"<a href='javascript:gotoRecord(0)'>"+i18n.say( "First" )+"</a>":i18n.say( "First" )%>
		</td>
		<td>
		<%=(first>0)?"<a href='javascript:gotoRecord("+((first-size)<0?0:(first-size))+")'>"+i18n.say( "Previous" )+"</a>":i18n.say( "Previous" )%>
		</td>
		<td>
		<%=(first+size>=totalNumber)?i18n.say( "Next" ):"<a href='javascript:gotoRecord("+(first+size)+")'>"+i18n.say( "Next" )+"</a>"%>
		</td>
		<td>
		<%=(first+size>=totalNumber)?i18n.say( "End" ):"<a href='javascript:gotoRecord("+((totalNumber-1)/size)*size+")'>"+i18n.say( "End" )+"</a>"%>
		</td>
		<td><%=first+1 %>~<%=(first+size)>totalNumber?totalNumber:first+size %> of <%=totalNumber %></td>
		<td>
		<%=i18n.say( "Page" )%>: <%=(first)/size+1 %>/<%=(totalNumber-1)/size+1 %> <input type="text" name="page" size="3" />
		<input type="submit" value="Go"/></td>
	</tr>