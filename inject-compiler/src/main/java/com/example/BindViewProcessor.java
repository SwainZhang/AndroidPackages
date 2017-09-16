package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by MyPC on 2017/3/15.
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {
    /**
     *
     */
    private Elements elementUtils;
    private Types typeUtils;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        mFiler = processingEnvironment.getFiler();
    }

    /**
     * the annotation you want to operate
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportAnnotationType = new HashSet<>();
        supportAnnotationType.add(BindView.class.getCanonicalName());
        return supportAnnotationType;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //activity-->key,the field including @BindView annotation -->value
        Map<TypeElement, List<FieldViewBind>> targetMap = new HashMap<>();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            List<FieldViewBind> list = targetMap.get(enclosingElement);
            if (list == null) {
                list = new ArrayList<>();
                targetMap.put(enclosingElement, list);
            }
            String packageName = getPackageName(enclosingElement);
            int resId = element.getAnnotation(BindView.class).value();
            String fieldName = element.getSimpleName().toString();
            TypeMirror typeMirror = element.asType();
            FieldViewBind fieldViewBind = new FieldViewBind(fieldName, typeMirror, resId);
            list.add(fieldViewBind);
        }

        Set<Map.Entry<TypeElement, List<FieldViewBind>>> entries = targetMap.entrySet();
        for (Map.Entry<TypeElement, List<FieldViewBind>> entry : entries) {
            List<FieldViewBind> list = entry.getValue();
            if (list == null || list.isEmpty()) {
                continue;
            }
            //typeElement is mainctivity
            TypeElement typeElement = entry.getKey();
            String packageName = getPackageName(typeElement);
            //get a String named MainActivity
            String complite = getClassName(typeElement, packageName);
            //generate a class named MainActivity information
            ClassName className = ClassName.bestGuess(complite);
            //generaate a interfcae name ViewBinder information
            ClassName viewBinder = ClassName.get("com.emery.test.inject", "ViewBinder");
            //build a Java class File
            TypeSpec.Builder classResult = TypeSpec
                    //class name MainActivity$$ViewBinder
                    .classBuilder(complite + "$$ViewBinder")
                    //method keyword public
                    .addModifiers(Modifier.PUBLIC)
                    // public MainActivity$$ViewBinder<T extends MainActivity>
                    .addTypeVariable(TypeVariableName.get("T", className))
                    //public MainActivity$$ViewBinder<T extends MainActivity> implements
                    // ViewBinder<MainActivity>
                    .addSuperinterface(ParameterizedTypeName.get(viewBinder, className));


            /**
             * public void binder(final MainActivity target){
             *
             * }
             */
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("binder")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(className, "target", Modifier.FINAL);

            /**
             * public void binder(final MainActivity target){
             *   target.title=(TextView)target.findViewById(R.id.title)
             * }
             */
            for (int i = 0; i < list.size(); i++) {
                FieldViewBind fieldViewBind = list.get(i);
                //android.widget.TextView
                String packageNameString = fieldViewBind.getTypeMirror().toString();
                ClassName textView = ClassName.bestGuess(packageNameString);
                //$L  stands for a placeholder
                methodBuilder.addStatement("target.$L=($T)target.findViewById($L)", fieldViewBind
                        .getName(), fieldViewBind.getTypeMirror(), fieldViewBind.getResId());

            }


            //add a method into class
            classResult.addMethod(methodBuilder.build());
            try {
                //generate a JavaFile from above
                JavaFile.builder(packageName, classResult.build()).addFileComment("auto generate " +
                        "file").build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    // MainActivity$$ViewBinder
    private String getClassName(TypeElement typeElement, String packageName) {
        int packageLength = packageName.length() + 1;
        return typeElement.getQualifiedName().toString().substring(packageLength).replace(".", "$");
    }

    private String getPackageName(TypeElement enclosingElement) {
        return elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
    }
}
