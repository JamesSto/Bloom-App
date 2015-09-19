#returns value from 0 to 1 based on strength of comparison


def compareInterests(interests1, interestRating1, interests2, interestRating2):
    numMatches = 0
    score = 0
    for item in interests1:
        if item in interests2:
            numMatches += 1
            score += (interestRating1[interests1.index(item)]
                + interestRating2[interests2.index(item)])

    return score/(numMatches * 10)            


def compareRating(rating1, rating2):
    score = (4 - abs(rating1 - rating2))/4

def compareBinary(val1, val2):
    return 1 if val1 = val2 else 0

def compareBubbleSelection(dict1, dict2):
    return sum(4 - abs(dict1[val] - dict2[val]) for val in dict1.keys())/(len(dict1))
