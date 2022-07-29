<?php

if (!$login || !$connect)
    exit(0);
    
if ($armymem['clan'] > 0 && $armymem['id'] == $clan['master']) {
    echo '<h2>Thiết lập đội</h2>';
    if ($_POST['save-desc']) {
    
        $text = $_POST['clan-desc'];
        $sql = "UPDATE `clan` SET `desc` = '". $conn->real_escape_string($text) ."' WHERE `id` = '". $armymem['clan'] ."'; ";
        $conn->query($sql);
        $clan['desc'] = $text;
        echo "\n" . '<h5>Lưu thành công.</h5>';
    
    } else if ($_POST['open-mem']) {
    
        if ($clan['luong'] >= 100 && $clan['memMax'] < 100) {
        
            $sql = "UPDATE `clan` SET `memMax` = `memMax` + 5, `luong` = `luong` - 100 WHERE `id` = '". $armymem['clan'] ."'; ";
            $conn->query($sql);
            echo "\n" . '<h5>Mở thêm thành viên thành công.</h5>';
    
        } else
            
            echo "\n" . '<h5 style="color:red;">* Mở thêm thất bại.</h5>';
    
    }
    echo '
    Email: '. $clan["email"] .'
    <br />
    Phone: '. $clan["phone"] .'
    <br />
    <form action="" method="post">
    Giới thiệu (tối đa 200 kí tự):
    <br />
    <textarea name="clan-desc" rows="2" cols="40">'. $clan["desc"] .'</textarea>
    <br />
    <input type="submit" name="save-desc" value="Cập nhật">
    <br />
    <br />
		  </form>
    ';
} else header('Location: ?c=list');

?>