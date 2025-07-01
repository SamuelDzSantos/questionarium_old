import time

import cv2
from homography import homography

def process_answer_sheet(image, num_rows, num_cols=6, marked_threshold=0.6, debug=False):

    start = time.time()
    #Warping perspective
    warped = homography(image, debug)

    #Denoising
    denoised = cv2.fastNlMeansDenoising(cv2.cvtColor(warped, cv2.COLOR_BGR2GRAY), None, 10, 7, 21)

    # Blurring
    #gray = cv2.cvtColor(denoised, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(denoised, (5, 5), 0)

    # Apply adaptive thresholding
    thresh = cv2.adaptiveThreshold(blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                   cv2.THRESH_BINARY_INV, 151, 5)

    if debug:
        cv2.imshow('Threshold', thresh)
        cv2.waitKey(1000)
        cv2.destroyAllWindows()

    # Find contours
    contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    contours = sorted(contours, key=cv2.contourArea, reverse=True)
    table_contour = contours[0] if contours else None

    if table_contour is None:
        print("No table contour found!")
        return []

    # Get bounding rectangle of the table
    x, y, w, h = cv2.boundingRect(table_contour)

    # Crop the image to the table
    table_image = thresh[y:y + h, x:x + w]
    if debug:
        cv2.imshow('Table cropped', table_image)
        cv2.waitKey(1000)
        cv2.destroyAllWindows()

    # Define the expected number of rows and columns
    cell_height = h // num_rows
    cell_width = w // num_cols

    if debug:
        # Inserting lines and text for debug
        img_copy = table_image.copy()
        for i in range(cell_height, h, cell_height):
            img_copy = cv2.putText(img_copy, f'{i}', (0,i-2), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255,255,255), 1, cv2.LINE_AA)
            cv2.line(img_copy, (0, i), (w, i), (255, 255, 0), 2)
        for i in range(cell_width, w, cell_width):
            cv2.line(img_copy, (i, 0), (i, h), (255, 255, 0), 2)

        cv2.imshow('lines', img_copy)
        cv2.waitKey(1000)
        cv2.destroyAllWindows()

    # Initialize results list
    results = []

    # Process each row
    for row in range(0, num_rows):
        question_number = row
        marked_answers = []

        # Check each answer cell in the row
        for col in range(1, num_cols):
            cell = table_image[row * cell_height:(row + 1) * cell_height,
                   col * cell_width:(col + 1) * cell_width]

            # Calculate the percentage of filled pixels
            total_pixels = cell.size
            filled_pixels = cv2.countNonZero(cell)
            filled_percentage = filled_pixels / total_pixels

            if debug:
                print(f"Question {row}, Option {chr(64 + col)}: {filled_percentage:.2f}")

            # If the cell is marked above the threshold, add it to marked_answers
            if filled_percentage > marked_threshold:
                marked_answers.append(chr(64 + col))  # Convert column index to letter (A, B, C, D, E)

        # Determine the final answer based on marked_answers
        if len(marked_answers) == 1:
            final_answer = marked_answers[0]
        elif len(marked_answers) > 1:
            final_answer = 'X'
        else:
            final_answer = 'X'

        results.append((question_number+1, final_answer))

        if debug:
            print(f"Question {question_number}: Marked answers: {marked_answers}, Final answer: {final_answer}")
    print(f'Elapsed: {time.time() - start}')
    return results

#Testing
#image = cv2.imread(r'C:\Users\--\--\good_angle_good_light_c_mk.png', cv2.IMREAD_COLOR)
#option = input('Insert debug mode: 0 for no debug 1 for debug \n')
#answers = process_answer_sheet(image, 15, 6, marked_threshold=0.7, debug=int(option))
#print(answers)
