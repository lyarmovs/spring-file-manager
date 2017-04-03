<!DOCTYPE html>
<html lang="en">
<head>

</head>
<body>

<p>The following documents were uploaded</p>
<p>----------------------------------------------------</p>
<p>Start Date: ${startDate?string('dd.MM.yyyy HH:mm:ss')}</p>
<p>End Date: ${endDate?string('dd.MM.yyyy HH:mm:ss')}</p>
<p>----------------------------------------------------</p>
<table>
<#list documentMetadataList as documentMetadata>
    <tr><td>Id:</td><td>${documentMetadata.id}</td></tr>
    <tr><td>Name:</td><td>${documentMetadata.name}</td></tr>
    <tr><td>Description:</td><td>${documentMetadata.description}</td></tr>
    <tr><td>Content Type:</td><td>${documentMetadata.contentType}</td></tr>
    <tr><td>Size:</td><td>${documentMetadata.size}</td></tr>
    <tr><td>Extension:</td><td>${documentMetadata.extension}</td></tr>
    <tr><td>Create Date:</td><td>${documentMetadata.createDate?string('dd.MM.yyyy HH:mm:ss')}</td></tr>
    <tr><td></td><td></td></tr>
</#list>
</table>
</body>
</html>