<?php
if (!$login || !$connect)
    exit(0);    
if ($armymem['clan'] > 0 && $armymem['clan'] == $clan['id']) {
    $page     = isset($_GET['p']) ? ( !preg_match('/^[0-9]+$/',$_GET['p']) ? 1 : ($_GET['p'] > 0 ? $_GET['p'] : 1 ) ) : 1;
    $limit       = $conn->real_escape_string(isset($_GET['limit']) ? $_GET['limit'] : 20);
    $order     = $conn->real_escape_string(isset($_GET[order]) ? $_GET[order] : 'id');
    $type       = $conn->real_escape_string(isset($_GET[type]) ? $_GET[type] : 'ASC');
    $start       = ($page - 1) * $limit;
    $num_rights = $conn->query("SELECT `id` FROM `clanmem` WHERE (`rights` > 0 AND `user` <> '". $clan['master'] ."' AND `clan` = '". $clan['id'] ."'); ")->num_rows;
    echo '<h2>Danh sách thành viên đội</h2>';
    $result = $conn->query("SELECT * FROM `clanmem` WHERE  `clan` = '". $clan['id'] ."' ORDER BY `rights` DESC, `$order` $type LIMIT $start, $limit");
    $i = $start+1;
    while ($member = $result->fetch_assoc()) {
        $aresult = $conn->query("SELECT * FROM `armymem` WHERE  `id` = '". $member['user'] ."' LIMIT 1");
        $amem = $aresult->fetch_assoc();
        $nvmem = json_decode($amem['NV'.$amem['nvXPMax']],true);     
        $XPmax     = 500 * $nvmem['lever'] * ($nvmem['lever'] + 1);
        $percentXP = (int) (($nvmem['xp'] * 100) / $XPmax);
        $nvname = $conn->query("SELECT * FROM `nhanvat` WHERE  `nhanvat_id` = '". $amem['nvXPMax'] ."' LIMIT 1")->fetch_assoc()['name'];
        echo $i. '
            <b style="color:blue">'. $member["user_name"] .'</b>
            <br /><b style="color:red">'. ($member["user"] == $clan["master"] ? 'Đội trưởng' : '<a style="color:red" href="./memberlist/refreshmaster.php?id='. $member["user"] .'">Nhường chức</a>') .'</b>
            <br />Điểm đội: '. $amem["clanpoint"] .', '. $member["xu"] .' xu, '. $member["luong"] .' lượng
            <br />Max: <b>'. $nvname .'</b>(Cấp '. $nvmem["lever"] .' + '. $percentXP .'%)
            <br />Cách đây '. string_tempo_restas(date("Y-m-d H:i:s"), $amem["lastOnline"]) .'
            <b style="color:blue">
        ';    
         if ($member['user'] != $clan['master']  && $clan['master'] == $armymem['id']) {
             if ($member['rights'] > 0)
                 echo "\n" . '<a href="./memberlist/rights.php?id='. $member["user"] .'">Thành viên</a> - ';
             else if ($num_rights < 4)
                 echo "\n" . '<a href="./memberlist/rights.php?id='. $member["user"] .'">Đội Phó</a> - ';
         }
         if ($member['user'] != $clan['master'] && ($clan['master'] == $armymem['id'] || ($clanmem['rights'] > 0 && $member['rights'] == 0)))
             echo "\n" . '<a href="./memberlist/kick.php?id='. $member["user"] .'">Đuổi</a>';
         echo '</b><br /><br />';
         $i++;
    }
?>
<tr>
</tbody>
</table>
<?php
   print('
        <a href="?c=info&f=memberlist&p='. ($page - 1) .'">&lt; Trước</a> • 
        <a href="?c=info&f=memberlist&p='. ($page + 1) .'">Sau &gt;</a>
   ');
}
?>