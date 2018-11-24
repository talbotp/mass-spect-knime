# Mass Spectrometry Data Analytics in Knime #

Here is the git repository for :

    Name: P Talbot
    Project: Mass Spectrometry Data Analytics in Knime.
    
## Scope ##
    
The scope of this project is to explore and build basic pre-pcoessing methods typical to the field of MSI and signal processing. We will explore visualization techniques used in the pre existin software “Spectral Analysis” and “MSIQuant”. Both of these software applications have moresophisticated visualization tools than we will have, due to the short time that we have, so for our purposes we will implement basic nodes. The scope of this project involves exploring clustering techniques in MSI. Our aim to explore k-means, DBSCAN, Normalized Cuts Image Segmentation, Self Organizing Maps and ToMaTo for Topological clustering.

## Structure ##

Here is the structure of this project:

* logs              weekly logs and documentation of work.
* msk_dev           source code and project testing.
    * src               source files.
    * docs              project documentation.
    * icons             contains png images of icons used.
    * test_workflows    contains KNIME test workflows using KNIME's testing framework.
* preseentation     project presentation.
* proposal          project proposal tex and pdf.
* report            latex and assets of report, tex and pdf.
* msk-jar           contains working jar.

## Reading imzML ##
 
There are multiple ways to load imzML into KNIME.

* Full Data      where we load a full imzML dataset into KNIME.
* Loop Start     where we load a fixed amount of spectra in loops (chunks).

## Preprocessing ##

Here are some of the preprocessing methods implemented:

* Rebinning
* Baseline Correction
    * Minimum
    * Median
    * Morphological Operators
* Smoothing
    * Moving Mean
    * Triangular Moving Mean
    * Savitzky-Golay Filter
* Normalization
    * Total Ion Count (TIC)
    * Mean
    * Euclidean Norm
* Regions of Interest (ROI)
    * Rectangular
* Spectral Representations
    * Average Spectrum
    * Basepeak Spectrum
* Limit m/z Range

## Visualizations ## 

Here are visualization techniques we have employed:

* Spectrum
* Multiple Spectra
* Ion Image using grayscale to represent the intensities.
* Cluster Viewer

### Dependencies ### 

We are dependent on the following java libraries. Note that these are modular, so only specific nodes will rely on each library.

* apache 
    * commons-math3-3.6.1.jar
    * commons-math3-3.6.1-tools.jar
* imageJ
    * imglib2
* imzMLConverter.jar
* javaml-0.1.7.jar
* JFreeChart
    * jcommon-1.0.23.jar
    * jfreechart-1.0.19.jar
* flanagan.jar









    
