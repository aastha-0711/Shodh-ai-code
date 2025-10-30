from flask import Flask, request, jsonify
import requests

app = Flask(__name__)

EXECUTOR_URL = "http://executor:6000/run"

@app.route("/run", methods=["POST"])
def judge_submission():
    data = request.get_json()
    code = data.get("code", "")
    language = data.get("language", "java")
    test_cases = data.get("testCases", [])

    results = []
    passed = 0

    for i, t in enumerate(test_cases, start=1):
        try:
            res = requests.post(EXECUTOR_URL, json={
                "language": language,
                "code": code,
                "input": t["input"]
            }, timeout=10)

            if res.status_code != 200:
                results.append({
                    "testCase": i,
                    "status": "Error",
                    "details": res.text
                })
                continue

            out = res.json()
            actual = out.get("stdout", "").strip()
            expected = t["expected"].strip()

            if actual == expected:
                passed += 1
                results.append({"testCase": i, "status": "Passed"})
            else:
                results.append({
                    "testCase": i,
                    "status": "Failed",
                    "expected": expected,
                    "got": actual
                })

        except Exception as e:
            results.append({
                "testCase": i,
                "status": "Error",
                "details": str(e)
            })

    return jsonify({
        "passed": passed,
        "total": len(test_cases),
        "status": "Accepted" if passed == len(test_cases) else "Wrong Answer",
        "details": results
    })


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
