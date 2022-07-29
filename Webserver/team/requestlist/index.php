<?php
if (!$login || !$connect)
    exit(0);
if ($armymem['clan'] > 0 && $clanmem['rights'] > 0) {
    $page     = isset($_GET['p']) ? ( !preg_match('/^[0-9]+$/',$_GET['p']) ? 1 : ($_GET['p'] > 0 ? $_GET['p'] : 1 ) ) : 1;
    $limit       = $conn->real_escape_string(isset($_GET['limit']) ? $_GET['limit'] : 10);
    $order     = $conn->real_escape_string(isset($_GET[order]) ? $_GET[order] : 'clanpoint');
    $type       = $conn->real_escape_string(isset($_GET[type]) ? $_GET[type] : 'DESC');
    $start       = ($page - 1) * $limit;
    $time    = date("Y-m-d H:i:s");
    echo '<h2>Danh sách xin gia nhập</h2><table border="0"><tbody>'; 
    
    $result = $conn->query("SELECT * FROM `armymem` WHERE  (`request_clan` = '". $clan['id'] ."' AND `clan` = 0) ORDER BY `$order` $type LIMIT $start, $limit");
    $i = $start+1;
    while ($member = $result->fetch_assoc()) {
        $uresult = $conn->query("SELECT `user`,`user_id` FROM `user` WHERE `user_id` = '". $member['id'] ."' LIMIT 1; ");
        $umem = $uresult->fetch_assoc();
        $nvmem = json_decode($member['NV'.$member['NVused']],true);
        $nvname = $conn->query("SELECT * FROM `nhanvat` WHERE  `nhanvat_id` = '". $member['NVused'] ."' LIMIT 1")->fetch_assoc()['name'];
        echo '
        <tr>
            <td>
                '. $i .': <b style="color:blue">'. $umem["user"] .'</b>
                <br />Điểm đội:'. $member["clanpoint"] .',xu:'. $member["xu"] .',nv:'. $nvname .',cấp độ:'. $nvmem["lever"] .'
                <br />'. ($clan["mem"] < $clan["memMax"] ? ('<a href="./requestlist/decision.php?type=duyet&id='. $umem["user_id"] .'">Đồng ý</a>'):'Đã tối đa') .' | <a href="./requestlist/decision.php?type=tc&id='. $umem["user_id"] .'">Từ chối</a>
            </td>
        </tr>
        ';
        $i++;
    }
    echo '
       </tbody>
       </table>
        <a href="?c=info&f=requestlist&p='. ($page - 1) .'">&lt; Trước</a> • 
        <a href="?c=info&f=requestlist&p='. ($page + 1) .'">Sau &gt;</a>
   ';
}
?>