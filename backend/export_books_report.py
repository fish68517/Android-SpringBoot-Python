import os
import sqlite3
import pandas as pd

DB_PATH = os.path.join("data", "books_trading.db")
OUT_HTML = "books_report.html"

def pick_book_table(conn) -> str:
    tables = pd.read_sql(
        "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;", conn
    )["name"].tolist()

    if not tables:
        raise RuntimeError("数据库里没有任何表。")

    print("数据库表：", tables)

    # 优先找名字里包含 book 的表
    candidates = [t for t in tables if "book" in t.lower()]
    if candidates:
        return candidates[0]

    # 常见候选名兜底
    for guess in ["books", "book", "tb_book", "book_table"]:
        if guess in tables:
            return guess

    # 找不到就用第一张表（你也可以改成 raise）
    return tables[0]

def main():
    if not os.path.exists(DB_PATH):
        raise FileNotFoundError(f"找不到数据库文件：{DB_PATH}")

    conn = sqlite3.connect(DB_PATH)

    table = pick_book_table(conn)
    print("使用表：", table)

    df = pd.read_sql(f"SELECT * FROM {table};", conn)
    conn.close()

    # 生成 HTML 表格
    table_html = df.to_html(index=False, escape=True)

    html = f"""
<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Books Report</title>
  <style>
    body {{ font-family: Arial, "Microsoft YaHei", sans-serif; padding: 16px; }}
    h2 {{ margin: 0 0 12px; }}
    .bar {{ display:flex; gap:12px; align-items:center; flex-wrap:wrap; margin-bottom: 12px; }}
    input {{ padding: 10px; width: 360px; max-width: 100%; }}
    .hint {{ color:#666; font-size: 12px; }}
    table {{ border-collapse: collapse; width: 100%; }}
    th, td {{ border: 1px solid #ddd; padding: 8px; vertical-align: top; }}
    th {{ position: sticky; top: 0; background: #f5f5f5; cursor: pointer; }}
    tr:nth-child(even) {{ background: #fafafa; }}
    .footer {{ margin-top: 10px; color:#666; font-size: 12px; }}
  </style>
</head>
<body>
  <h2>Books Report（表：{table}）</h2>

  <div class="bar">
    <input id="q" placeholder="输入关键字过滤（支持任意列）" onkeyup="filterRows()" />
    <span class="hint">提示：点击表头可按该列排序</span>
  </div>

  <div id="tableWrap">
    {table_html}
  </div>

  <div class="footer" id="count"></div>

<script>
function filterRows(){{
  const q = document.getElementById('q').value.toLowerCase();
  const rows = document.querySelectorAll('table tbody tr');
  let visible = 0;
  rows.forEach(r => {{
    const text = r.innerText.toLowerCase();
    const show = text.includes(q);
    r.style.display = show ? '' : 'none';
    if (show) visible++;
  }});
  document.getElementById('count').innerText = `显示条数：${{visible}} / ${{rows.length}}`;
}}
filterRows();

// 简单排序：点击表头排序（字符串排序；数字列也能用但不完美）
document.querySelectorAll('table thead th').forEach((th, idx) => {{
  th.addEventListener('click', () => sortTable(idx));
}});

let sortDir = {{}};
function sortTable(colIdx){{
  const table = document.querySelector('table');
  const tbody = table.tBodies[0];
  const rows = Array.from(tbody.rows).filter(r => r.style.display !== 'none');

  sortDir[colIdx] = !sortDir[colIdx];
  const asc = sortDir[colIdx];

  rows.sort((a,b) => {{
    const A = a.cells[colIdx]?.innerText.trim() ?? '';
    const B = b.cells[colIdx]?.innerText.trim() ?? '';
    // 尝试数字
    const nA = parseFloat(A.replace(/[^0-9.-]/g,''));
    const nB = parseFloat(B.replace(/[^0-9.-]/g,''));
    const bothNum = !isNaN(nA) && !isNaN(nB);

    if (bothNum) return asc ? (nA - nB) : (nB - nA);
    return asc ? A.localeCompare(B) : B.localeCompare(A);
  }});

  // 重新插入排序后的行
  rows.forEach(r => tbody.appendChild(r));
}}
</script>
</body>
</html>
"""

    with open(OUT_HTML, "w", encoding="utf-8") as f:
        f.write(html)

    print(f"✅ 导出完成：{OUT_HTML}（双击用浏览器打开）")

if __name__ == "__main__":
    main()