// src/main/resources/static/js/drawing.js

window.addEventListener('DOMContentLoaded', () => {
  const canvas = document.getElementById('drawing-canvas');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');

  // 캔버스 전체 크기를 윈도우 크기에 맞춤
  function resizeCanvas() {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
  }
  window.addEventListener('resize', resizeCanvas);
  resizeCanvas();

  // 그리기 상태
  let drawing = false;
  let lastX = 0, lastY = 0;

  // 마우스 다운 → 그리기 시작
  canvas.addEventListener('mousedown', e => {
    drawing = true;
    lastX = e.clientX;
    lastY = e.clientY;
  });
  // 마우스 이동 → 선 그리기
  canvas.addEventListener('mousemove', e => {
    if (!drawing) return;
    ctx.strokeStyle = 'red';
    ctx.lineWidth = 2;
    ctx.lineCap = 'round';
    ctx.beginPath();
    ctx.moveTo(lastX, lastY);
    ctx.lineTo(e.clientX, e.clientY);
    ctx.stroke();
    lastX = e.clientX;
    lastY = e.clientY;
  });
  // 마우스 업/아웃 → 그리기 종료
  ['mouseup','mouseout'].forEach(evt =>
    canvas.addEventListener(evt, () => drawing = false)
  );

  // clearCanvas 함수: window.clearCanvas()로 호출
  window.clearCanvas = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
  };
});
