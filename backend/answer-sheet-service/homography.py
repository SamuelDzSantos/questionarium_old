import time
import cv2
import numpy as np
from matplotlib import pyplot as plt

# Reading images
img_reference = cv2.imread(r'./assets/img_reference_tg.png', cv2.IMREAD_COLOR)
img_reference = cv2.cvtColor(img_reference, cv2.COLOR_BGR2RGB)
assert img_reference is not None, "file could not be read, check with os.path.exists()"

def homography(img_to_align, debug=False):
    start = time.time()
    img_to_align = cv2.cvtColor(img_to_align, cv2.COLOR_BGR2RGB)
    assert img_to_align is not None, "file could not be read"

    # Resizing to reference image size
    img_to_align = cv2.resize(img_to_align, (img_reference.shape[1], img_reference.shape[0]),
                              interpolation=cv2.INTER_AREA)

    if debug:
        # Printing original images
        plt.figure(figsize=[20, 10])
        plt.subplot(121);plt.axis('off');plt.imshow(img_reference, cmap='gray');plt.title('Original image')
        plt.subplot(122);plt.axis('off');plt.imshow(img_to_align, cmap='gray');plt.title('Scanned image')
        plt.show()

    # Finding keypoints
    img_reference_gray = cv2.cvtColor(img_reference, cv2.COLOR_BGR2GRAY)
    img_to_align_gray = cv2.cvtColor(img_to_align, cv2.COLOR_BGR2GRAY)

    #MAX_NUM_FEATURES = 500 # ORB PARAMETER

    #orb = cv2.ORB_create(MAX_NUM_FEATURES)
    #keypoints1, descriptors1 = orb.detectAndCompute(img_reference_gray, None)
    #keypoints2, descriptors2 = orb.detectAndCompute(img_to_align_gray, None)

    sift = cv2.SIFT_create()
    keypoints1, descriptors1 = sift.detectAndCompute(img_reference_gray, None)
    keypoints2, descriptors2 = sift.detectAndCompute(img_to_align_gray, None)

    if debug:
        print(f"Keypoints in reference image: {len(keypoints1)}")
        print(f"Keypoints in image to align: {len(keypoints2)}")

    # Draw keypoints
    img_reference_display = cv2.drawKeypoints(img_reference_gray, keypoints1, outImage=np.array([]), color=(255, 0, 0),
                                              flags=cv2.DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)
    img_to_align_display = cv2.drawKeypoints(img_to_align_gray, keypoints2, outImage=np.array([]), color=(255, 0, 0),
                                             flags=cv2.DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)

    if debug:
        # Display keypoints
        plt.figure(figsize=[20, 10])
        plt.subplot(121);plt.axis('off');plt.imshow(img_reference_display);plt.title('Keypoints Original image')
        plt.subplot(122);plt.axis('off');plt.imshow(img_to_align_display);plt.title('Keypoints Scanned image')
        plt.show()

    '''# Match features
    matcher = cv2.DescriptorMatcher_create(cv2.DESCRIPTOR_MATCHER_BRUTEFORCE_HAMMING)
    matches = list(matcher.match(descriptors1, descriptors2, None))'''

    # Match features using BFMatcher for SIFT
    matcher = cv2.BFMatcher(cv2.NORM_L2, crossCheck=False)
    matches = matcher.match(descriptors1, descriptors2)

    '''matches.sort(key=lambda x: x.distance, reverse=False)'''
    matches = sorted(matches, key=lambda x: x.distance)

    numGoodMatches = int(len(matches) * 0.1)
    matches = matches[:numGoodMatches]

    if debug:
        print(f'Total Matches: {len(matches)}')
        im_matches = cv2.drawMatches(img_reference_display, keypoints1, img_to_align_gray, keypoints2, matches, None)
        plt.figure()
        plt.imshow(im_matches);plt.axis('off');plt.title('Matched Keypoints')
        plt.show()

    points1 = np.zeros((len(matches), 2), dtype=np.float32)
    points2 = np.zeros((len(matches), 2), dtype=np.float32)

    for i, match in enumerate(matches):
        points1[i, :] = keypoints1[match.queryIdx].pt
        points2[i, :] = keypoints2[match.trainIdx].pt

    # Estimate affine transformation
    if len(matches) >= 3:
        affine_matrix, mask = cv2.estimateAffine2D(points2, points1)

        # Apply the affine transformation
        im2_reg = cv2.warpAffine(img_to_align, affine_matrix, (img_reference.shape[1], img_reference.shape[0]))
    else:
        print("Not enough matches to compute an affine transformation.")
        im2_reg = img_to_align

    if debug:
        plt.figure()
        plt.subplot(131);plt.imshow(img_reference);plt.title('Reference Image');plt.axis('off')
        plt.subplot(132);plt.imshow(img_to_align);plt.title('Image to Align');plt.axis('off')
        plt.subplot(133);plt.imshow(im2_reg);plt.title('Aligned Image');plt.axis('off')
        plt.show()
        end = time.time()
        print(f'Time elapsed: {end - start}')

    return im2_reg

# Testing
#img_to_align = cv2.imread(r'C:\Users\aalex\Downloads\good_angle_good_light_c_mk.png', cv2.IMREAD_COLOR)
#homography(img_to_align, True)
