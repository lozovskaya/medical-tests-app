from flask import Flask, jsonify, request
from typing import List, Tuple

app = Flask(__name__)


def parse_file(data: bytes) -> List[Tuple[str, str, str]]:
    return [("a", "b", "C"), ("a", "b", "d")]


@app.route("/", methods=["POST"])
def process():
    if "file" not in request.files:
        return "No file part in the request", 401

    file = request.files["file"]

    if not file.filename:
        return "No selected file", 400

    input_data = file.read()

    parsed_data = parse_file(data=input_data)

    return jsonify(parsed_data), 200


if __name__ == '__main__':
    app.run()
