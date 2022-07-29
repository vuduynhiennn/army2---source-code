<?php

if (!$login || !$connect)
    exit(0);
    
include 'res/head.php';

?>

<br />
<a href="?c=list">Tham gia đội</a>
<br />
<br />
<a href="?c=create">Tạo mới Đội</a>
<br />
<br />

<?php

include 'res/end.php';
?>