<html>
<body>
<h2>Upload your files</h2>
<form action="/input" method="post" enctype="multipart/form-data">
    <label>
        <input type="text" name="description" />
    </label>
    <input type="file" name="file" multiple="true" />
    <input type="submit" />
</form>
</body>
</html>
