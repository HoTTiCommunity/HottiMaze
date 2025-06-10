// src/main/resources/static/js/bookmark-local.js

const STORAGE_KEY = 'hottimaze_bookmarks';

// localStorage에서 북마크 목록 읽기
function getBookmarks() {
  const raw = localStorage.getItem(STORAGE_KEY);
  return raw ? JSON.parse(raw) : [];
}

// 특정 ID가 북마크에 있는지 확인
function isBookmarked(id) {
  return getBookmarks().includes(id);
}

// localStorage에 북마크 배열 저장
function setBookmarks(arr) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(arr));
}

// 아이콘 클릭 시 토글
function toggleBookmark(el) {
  // 상세 페이지든 목록 페이지든, 아이콘 자신의 data-id를 읽도록
  const postId = el.getAttribute('data-id');
  let arr = getBookmarks();

  if (isBookmarked(postId)) {
    arr = arr.filter(x => x !== postId);
  } else {
    arr.push(postId);
  }
  setBookmarks(arr);
  updateIconState(el, postId);
}

// 아이콘 상태 업데이트 (★ 또는 ☆)
function updateIconState(el, postId) {
  if (isBookmarked(postId)) {
    el.textContent = '★';
    el.classList.add('bookmarked');
  } else {
    el.textContent = '☆';
    el.classList.remove('bookmarked');
  }
}

// 페이지 로드 시 초기 상태 세팅
window.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.bookmark-icon').forEach(icon => {
    const postId = icon.getAttribute('data-id');
    updateIconState(icon, postId);
  });
});
