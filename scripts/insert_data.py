import random
import time

import mysql.connector
from faker import Faker
from datetime import datetime

def create_connection():
    """데이터베이스 연결 생성"""
    try:
        connection = mysql.connector.connect(
            host='localhost',
            port=13306,
            database='codestream',
            user='jungmini',
            password='qwer1234',
            autocommit=False
        )
        return connection
    except mysql.connector.Error as err:
        print(f" Can't Connect Database: {err}")
        return None

def insert_categories(cursor):
    """카테고리 데이터 삽입"""
    categories = [
        ('Java', datetime.now(), datetime.now()),
        ('Spring', datetime.now(), datetime.now()),
        ('Database', datetime.now(), datetime.now()),
        ('Linux', datetime.now(), datetime.now()),
        ('Docker', datetime.now(), datetime.now()),
        ('AWS', datetime.now(), datetime.now()),
        ('Kubernetes', datetime.now(), datetime.now()),
    ]

    cursor.executemany("""
        INSERT INTO categories (name, created_at, updated_at)
        VALUES ( %s, %s, %s )
    """, categories)

    print("Category Data Created")

def bulk_insert_articles(cursor, batch_size = 10000):
    """게시글 데이터 대량 삽입"""
    fake = Faker('ko_KR')
    total_record = 0
    start_time = time.time()

    for batch in range(130):
        articles = []

        for i in range(batch_size):
            articles.append((
                # title
                fake.sentence(nb_words=5)[:100],
                # author
                fake.name()[:20],
                # desc
                fake.text(max_nb_chars=200)[:200],
                # thubmnailUrl
                fake.url()[:255],
                # link
                fake.url()[:255],
                # categoryId
                random.randint(1, 7),
                datetime.now(),
                datetime.now(),
            ))

        cursor.executemany("""
            INSERT INTO articles (title, author, description, thumbnail_url, link, category_id, created_at, updated_at)
            VALUES ( %s, %s, %s, %s, %s, %s, %s, %s )
        """, articles)

        total_record += batch_size

        # 10개 마다 커밋
        if batch % 10 == 0 and batch != 0:
            cursor.execute("COMMIT")
            executed_time = time.time() - start_time
            print(f"Progress: {total_record:,} records inserted. "
                  f"executed_time: {executed_time:.1f}s")

def main():
    print("Insert Test Data..")

    connection = create_connection()
    cursor = connection.cursor()

    # insert_categories(cursor)
    # cursor.execute("COMMIT")

    bulk_insert_articles(cursor)
    cursor.execute("COMMIT")

    print("Batch Terminated")

# 왜 자꾸 중간에 커넥션을 놓칠까?
if __name__ == "__main__":
    main()