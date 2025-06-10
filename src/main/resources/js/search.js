// src/main/resources/static/js/search.js

window.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('search-input');
  const button = document.getElementById('search-button');
  const table = document.querySelector('table');           // 첫 번째 테이블
  const tbody = table.querySelector('tbody');
  // 모든 데이터 행(<tr>)을 가져오되, "게시글 없음" 행은 나중에 처리
  const dataRows = Array.from(tbody.querySelectorAll('tr'))
    .filter(row => row.querySelector('td:nth-child(2)'));  // 제목 셀이 있는 행만

  // "게시글 없음" 메시지 행
  const noDataRow = tbody.querySelector('tr[th\\:if]')
    || tbody.querySelector('tr:last-child');

  function filterPosts() {
    const q = input.value.trim().toLowerCase();
    let anyVisible = false;

    dataRows.forEach(row => {
      const title = row.querySelector('td:nth-child(2)')
                       .textContent
                       .trim()
                       .toLowerCase();
      const show = title.includes(q);
      row.style.display = show ? '' : 'none';
      if (show) anyVisible = true;
    });

    // 결과가 하나도 없으면 "게시글 없음" 행을 보이게
    if (noDataRow) {
      noDataRow.style.display = anyVisible ? 'none' : '';
    }
  }

  input.addEventListener('keydown', e => {
    if (e.key === 'Enter') filterPosts();
  });
  button.addEventListener('click', filterPosts);
});
