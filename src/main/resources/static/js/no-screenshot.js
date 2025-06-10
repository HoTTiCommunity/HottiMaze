// 우클릭(컨텍스트 메뉴) 차단
document.addEventListener('contextmenu', function(e) {
  e.preventDefault();
});

// 특정 키(예: PrintScreen, Ctrl+S, Ctrl+P) 차단
document.addEventListener('keydown', function(e) {
  if (e.key === 'PrintScreen' || e.keyCode === 44) {
    e.preventDefault();
    alert('화면 캡처는 허용되지 않습니다.');
  }
  if ((e.ctrlKey || e.metaKey) && e.key === 's') {
    e.preventDefault();
    alert('페이지 저장을 차단합니다.');
  }
  if ((e.ctrlKey || e.metaKey) && e.key === 'p') {
    e.preventDefault();
    alert('페이지 인쇄를 차단합니다.');
  }
});

// 텍스트 선택(드래그) 방지
document.addEventListener('selectstart', function(e) {
  e.preventDefault();
});
