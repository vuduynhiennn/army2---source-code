<?php

if (!$login || !$connect || $clanmem['rights'] == 0)
    exit(0);
    
if ($armymem['clan'] > 0 && $armymem['clan'] == $clan['id']) {

    $sql = "SELECT * FROM `armymem` WHERE (`request_clan` = '". $clan['id'] ."' AND `clan` = 0); ";
    $result = $conn->query($sql);
    $num_request = $result->num_rows;

    $XPmax     = 25000 * $clan['level'] * ($clan['level'] + 1);
    $percentXP = (int) (($clan['xp'] * 100) / $XPmax);
        include 'res/head.php';
        
        echo "\n" . '<h2>Quản lý đội</h2><p>DỘI: <b>'. $clan["name"] .'</b></p>
        Số đội viên: '. $clan["mem"] .'/'. $clan["memMax"] .' <a href="?c=addmem">(+5 đội viên, giá <b style="color:red">100</b> lượng)</a>
        <br />Xu: '. $clan["xu"] .', Lượng: '. $clan["luong"] .'
        <br />Cup: '. $clan["cup"] .', Kinh Nghiệm: '. $clan["xp"] .' /  '. $XPmax .'
        <br />Cấp: '. $clan["level"] .'  +  '. $percentXP .' %
        <br />
          <a href="?c=info'. ($_GET["f"]=="memberlist"?'':'&f=memberlist') .'">Danh sách đội viên</a> |
          <a href="?c=info'. ($_GET["f"]=="requestlist"?'':'&f=requestlist') .'">Danh sách yêu cầu '. ($num_request>0?('+ '. $num_request): '') .'</a> |
          '. ($armymem['id'] == $clan['master'] ? '<a href="?c=info'. ($_GET["f"]=="establish"?'':'&f=establish') .'">Thiết lập</a> |' : '') .'
          <a href="?c=info'. ($_GET["f"]=="item"?'':'&f=item') .'">Item</a>
          
        ';
        switch ($_GET['f']) {
            case 'memberlist':
                include('./memberlist/index.php');
                break;
            case 'requestlist':
                include('./requestlist/index.php');
                break;
            case 'establish':
                include('./establish/index.php');
                break;
            case 'item':
                include('./item/index.php');
                break;
            
        }
        include 'res/end.php';
} else header('Location: ?c=list');

?>