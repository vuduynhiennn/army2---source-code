<?php
    function goURL($url)
    {
        header('Location:' . $url);
        exit(0);
    }
    
    function replace_str($str, $len)
    {
    return str_repeat('*', strlen($str)-$len).
    substr($str, strlen($str)-$len, strlen($str));
    }

    function string_tempo_restas($time_now, $time_old) {

    $s = abs(strtotime($time_now) - strtotime($time_old));
    
    if ($s >= 31104000)
        return FLOOR($s / 31104000)." năm";
        
    else if ($s >= 2592000)
        return FLOOR($s / 2592000)." tháng";
        
    else if ($s >= 604800)
        return FLOOR($s / 604800)." tuần";
        
    else if ($s >= 86400)
        return FLOOR($s / 86400)." ngày";
        
    else if ($s >= 3600)
        return FLOOR($s / 3600)." giờ";

    else if ($s >= 60)
        return FLOOR($s / 60)." phút";
        
    else return $s." giây";
    
}

?>