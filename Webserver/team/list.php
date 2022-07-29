<?php
if (!$login || !$connect)
    exit(0);
include 'res/head.php';

if ($armymem['request_clan'] > 0 || $armymem['clan'] > 0) {
    
    $sql = "SELECT * FROM `clan` WHERE (`id` = '". $armymem['clan'] ."' OR `id` = '". $armymem['request_clan'] ."') LIMIT 1; ";
    $result = $conn->query($sql);
    $clan = $result->fetch_assoc();
    if ($clan["master"] == $armymem["id"])
        header('Location: ?c=info');
    echo '
        <table>
            <tbody>
                <tr>
                    <td width="30px">
                        <b>Đội:</b>
                    </td>
                    <td colspan="2">
                        '. $clan["name"] .'
                    </td>
                <tr>
                    <td colspan="2">
                        '. ($clan["master"] != $armymem["id"] ? ('<a href="?c=outclan">Xin thoát đội</a>') : ('')) .'
                        ('. ($armymem["clan"] == $armymem["request_clan"] ? ('mất phí rời đội 1000 xu và 100 điểm cống hiến đội') : ('<i>Đang chờ duyệt</i>')) .')
                    </td>
                </tr>
                <tr>
                    '. (($armymem["clan"] > 0 && $clanmem["rights"] == 1) ? '<td colspan="2"><a href="?c=info">Xem Đội</a></td>' : '') .'
                <tr>
            </tbody>
        </table>
     ';

} else {

    $page     = isset($_GET['p']) ? ( !preg_match('/^[0-9]+$/',$_GET['p']) ? 1 : ($_GET['p'] > 0 ? $_GET['p'] : 1 ) ) : 1;
    $limit       = $conn->real_escape_string(isset($_GET['limit']) ? $_GET['limit'] : 15);
    $order     = $conn->real_escape_string(isset($_GET[order]) ? $_GET[order] : 'xp');
    $type       = $conn->real_escape_string(isset($_GET[type]) ? $_GET[type] : 'DESC');
    $start       = ($page - 1) * $limit;
    
    $clanname = $_POST['clanname'];
    $clancaptain = $_POST['clancaptain'];


    echo '
<form action="" method="post">
    Tên đội:<br>
    <input type="text" name="clanname" value="'. $clanname .'">
    <input type="submit" name="submit" value="Tìm">
</form>

<form action="" method="post">
    Tên đội trưởng:<br>
    <input type="text" name="clancaptain" value="'. $clancaptain .'">
    <input type="submit" name="submit" value="Tìm">
</form>

<h3>Danh sách đội</h3>
<table border="1">
<tbody>
<tr>
<td><b>Tên đội</b></td>
<td><b>Đội trưởng</b></td>
<td><b>Ngày tạo</b></td>
<td><b>Số đội viên</b></td>
<td></td>
</tr>

    ';
    
$result = $conn->query("SELECT * FROM `clan` WHERE (`name` LIKE '%". $conn->real_escape_string($clanname) ."%' AND `masterName` LIKE '%". $conn->real_escape_string($clancaptain) ."%') ORDER BY `$order` $type LIMIT $start, $limit");
while ($clanInfo = $result->fetch_assoc()) {
    echo '
    <tr>
        <td>'. $clanInfo["name"] .'</td>
        <td>'. $clanInfo["masterName"] .'</td>
        <td>'. date('d/m/Y', strtotime($clanInfo["dateCreat"])) .'</td>
        <td>'. $clanInfo["mem"] .'/'. $clanInfo["memMax"] .'</td>
        <td>'. ($clanInfo["mem"] < $clanInfo["memMax"] ? ('<a href="?c=join&id='. $clanInfo["id"] .'">Xin vào</a>') : '') .'</td>
    </tr>
    ';
    
}
?>

<tr>
</tbody>
</table>
<?php
   print('
        <a href="?c=list&p='. ($page - 1) .'">&lt; Trước</a> • 
        <a href="?c=list&p='. ($page + 1) .'">Sau &gt;</a>
   ');
}

include 'res/end.php';

?>