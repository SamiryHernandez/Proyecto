package com.uth.proyecto.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.uth.proyecto.LoginActivity;
import com.uth.proyecto.MenuMainActivity;
import com.uth.proyecto.R;
import com.uth.proyecto.adapters.CountryAdapter;
import com.uth.proyecto.classes.Utils;
import com.uth.proyecto.config.ApiConfig;
import com.uth.proyecto.models.CountryResponseModel;
import com.uth.proyecto.models.MeResponseModel;
import com.uth.proyecto.models.UserResponseModel;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PerfilFragment extends Fragment {
    private RequestQueue requestQueue;
    Utils utils = new Utils();
    EditText txtName, txtLastName, txtEmail, txtCurrentPassword, txtNewPassword, txtPasswordConfirm;
    Button btnUpdate, btnChangePassword, btnDelete,btnLogout;
    AutoCompleteTextView country;
    ImageView photo;
    List<CountryResponseModel.Country> countriesList = new ArrayList<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private CountryResponseModel.Country selectedCountry;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        init(view);
        loadData(view);
        return view;
    }

    private void loadData(@NonNull View v) {
        String accessToken = utils.getToken(v.getContext());

        JsonObjectRequest secondJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                ApiConfig.endpointMe, null,
                response -> {
                    Log.d("Respuesta", "Respuesta del servidor (Perfil): " + response.toString());
                    try {
                        if (!response.getBoolean("status")) {
                            utils.message(v, response.getString("message"));
                            return;
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    MeResponseModel meResponseModel = MeResponseModel.fromJson(response);
                    if (meResponseModel != null) {
                        boolean status = meResponseModel.isStatus();
                        UserResponseModel meDataModel = meResponseModel.getData();

                        if (status) {
                            txtName.setText(meDataModel.getProfile().getFirstName());
                            txtLastName.setText(meDataModel.getProfile().getLastName());
                            txtEmail.setText(meDataModel.getEmail());
                            country.setText(meDataModel.getProfile().getCountryName());

                            if (meDataModel.getProfile().getPhotoUrl() != null) {
                                String imageUrl = meDataModel.getProfile().getPhotoUrl();

                                RequestOptions requestOptions = new RequestOptions()
                                        .placeholder(R.drawable.perfil) // Imagen de placeholder mientras se carga
                                        .error(R.drawable.perfil) // Imagen de error si no se puede cargar la imagen
                                        .transform(new CircleCrop()); // Opcional: Aplicar un recorte circular a la imagen

                                Glide.with(this)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                // Manejo de errores, por ejemplo, mostrar un mensaje de error
                                                Log.e("Glide", "Error loading image", e);
                                                e.logRootCauses("Glide"); // Agrega esta línea para obtener más detalles en el log
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        })
                                        .into(photo);
                            }
                            // photo.setImageResource(R.drawable.perfil);
                        }
                    }

                },
                error -> {
                    utils.message(v, "Error al obtener datos del perfil.");
                    Log.e("Volley Error", error.toString());
                }) {
            @NonNull
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);

                return headers;
            }
        };

        Log.d("Campos", "Endpoint: " + ApiConfig.endpointMe);
        requestQueue.add(secondJsonObjectRequest);
    }


    //cambio de contraseña

    private void changePassword(String currentPassword, String newPassword, String newCPassword) {
        String accessToken = utils.getToken(requireContext());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("old_password", currentPassword);
            requestBody.put("password", newPassword);
            requestBody.put("password_confirmation", newCPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            utils.message(requireView(), "Contraseña cambiada exitosamente");
        }

        JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.PUT,
                ApiConfig.endpointpassword, requestBody,
                response -> {
                    Log.d("Respuesta", "Respuesta del servidor (Cambio de Contraseña): " + response.toString());
                    try {
                        if (!response.getBoolean("status")) {
                            utils.message(requireView(), response.getString("message"));
                            Log.d("Campos", "Current Password put: " + currentPassword);
                            Log.d("Campos", "New Password: " + newPassword);
                            Log.d("Campos", "Confirm Password: " + newCPassword);
                        }

                        //   Puedes realizar alguna acción adicional después de cambiar la contraseña, si es necesario.


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    utils.message(requireView(), "Error al cambiar la contraseña");
                    Log.e("Volley Error", error.toString());
                }) {
            @NonNull
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);

                return headers;
            }
        };

        Log.d("Campos", "Endpoint: " + ApiConfig.endpointpassword);
        Log.d("Campos", "Request Body: " + requestBody.toString());
        Toast.makeText(requireContext(), "Contraseña Actualizada Exitosamente", Toast.LENGTH_SHORT).show();
        requestQueue.add(changePasswordRequest);
    }

    // modificar datos de la cuenta

    private void updateProfile(String nombre, String Apellido, int Pais) {
        String accessToken = utils.getToken(requireContext());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("first_name", nombre);
            requestBody.put("last_name", Apellido);
            requestBody.put("country_id", Pais);
            //requestBody.put("photo", url);

        } catch (JSONException e) {
            e.printStackTrace();
            utils.message(requireView(), "Datos cambiados Exitosamente");
        }

        JsonObjectRequest changeprofiledata = new JsonObjectRequest(Request.Method.PUT,
                ApiConfig.endpointupdate, requestBody,
                response -> {
                    Log.d("Respuesta", "Respuesta del servidor (Actualizar Perfil): " + response.toString());
                    try {
                        if (!response.getBoolean("status")) {
                            utils.message(requireView(), response.getString("message"));
                            Log.d("Campos", "Nombre: " + nombre);
                            Log.d("Campos", "Apellido: " + Apellido);
                            Log.d("Campos", "Pais: " + Pais);
                            //Log.d("Campos", "photo: " + url);
                        }
                        // Puedes realizar alguna acción adicional después de cambiar la contraseña, si es necesario.

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    utils.message(requireView(), "Error al cambiar la contraseña");
                    Log.e("Volley Error", error.toString());
                }) {
            @NonNull
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);

                return headers;
            }
        };

        Log.d("Campos", "Endpoint: " + ApiConfig.endpointupdate);
        Log.d("Campos", "Request Body: " + requestBody.toString());
        Toast.makeText(requireContext(), "Datos Actualizados Exitosamente", Toast.LENGTH_SHORT).show();
        requestQueue.add(changeprofiledata);
    }

    public void init(@NonNull View v) {
        // EditText
        txtName = v.findViewById(R.id.txtPName);
        txtLastName = v.findViewById(R.id.txtPLastName);
        txtEmail = v.findViewById(R.id.txtPEmail);
        txtCurrentPassword = v.findViewById(R.id.txtPCurrentPassword);
        txtNewPassword = v.findViewById(R.id.txtPNewPassword);
        txtPasswordConfirm = v.findViewById(R.id.txtPPasswordConfirm);
//
//        // Spinner
        country = v.findViewById(R.id.txtCountry);
//
//        // ImageView
        photo = v.findViewById(R.id.txtImage);
//
//        // Buttons
        btnUpdate = v.findViewById(R.id.btnPUdate);
        btnChangePassword = v.findViewById(R.id.btnPChangePassword);
        btnDelete = v.findViewById(R.id.btnPDelete);
        btnLogout = v.findViewById(R.id.btnLogout);

        getCountries(v);

        btnChangePassword.setOnClickListener(view -> {
            String currentPassword = txtCurrentPassword.getText().toString();
            String newPassword = txtNewPassword.getText().toString();
            String confirmPassword = txtPasswordConfirm.getText().toString();

            // Validar que las contraseñas coincidan
            if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                utils.message(v, "Por favor, complete todos los campos.");
                return;
            }

            // Validar que las contraseñas coincidan
            if (!newPassword.equals(confirmPassword)) {
                utils.message(v, "Las contraseñas no coinciden");
                return;
            }

            // Realizar la solicitud de cambio de contraseña al servidor
            changePassword(currentPassword, newPassword, confirmPassword);

        });


        btnUpdate.setOnClickListener(view -> {
            String nombre = txtName.getText().toString();
            String apellido = txtLastName.getText().toString();
            //String phototico = photo.toString();
            int selectedCountryId = getSelectedCountryId();

            // Validate if a country is selected
            if (selectedCountryId == -1) {
                utils.message(view, "Por favor, seleccione un país.");
                return;
            }

            // Realizar la solicitud de cambio de perfil al servidor
            updateProfile(nombre, apellido, selectedCountryId);
            Toast.makeText(requireContext(), "Datos Actualizados Exitosamente", Toast.LENGTH_SHORT).show();
        });

        /* photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCamera();
            }
        }); */

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

    }

    private int getSelectedCountryId() {
        String selectedCountryName = country.getText().toString();
        for (CountryResponseModel.Country country : countriesList) {
            if (country.getName().equalsIgnoreCase(selectedCountryName)) {
                return country.getId();
            }
        }
        return -1;
    }


    protected void getCountries(@NonNull View v) {
        requestQueue = Volley.newRequestQueue(v.getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiConfig.endpointCountries, null, response -> {

            CountryResponseModel crm = CountryResponseModel.fromJson(response);

            if (crm != null) {
                countriesList = crm.getData();
                CountryAdapter countryAdapter = new CountryAdapter(v.getContext(), countriesList);
                country.setAdapter(countryAdapter);
            }
        }, error -> {
            Log.e("Volley Error", error.toString());
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void logout(@NonNull View v) {
        String accessToken = utils.getToken(v.getContext());

        JsonObjectRequest logoutRequest = new JsonObjectRequest(Request.Method.POST,
                ApiConfig.endpointlogout, null,
                response -> {
                    try {
                        if (response.getBoolean("status")) {
                            // Solicitud de cierre de sesión exitosa
                            // Limpiar el token localmente en Android
                            // Aquí puedes redirigir al usuario a la pantalla de inicio de sesión
                            // o cualquier otra pantalla relevante después de cerrar sesión.
                            // Redirigir al usuario a la pantalla de inicio de sesión

                            utils.clearToken(v.getContext());

                            // Eliminar la caché de Volley
                            requestQueue.getCache().clear();

                            // Eliminar archivos almacenados en el almacenamiento interno
                            clearInternalStorage();

                            // Redirigir al usuario a la pantalla de inicio de sesión
                            Intent intent = new Intent(requireContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finishAffinity();  // Cierra todas las actividades en la pila

                        } else {
                            utils.message(requireView(), response.getString("message"));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    utils.message(requireView(), "Error al cerrar sesión");
                    Log.e("Volley Error", error.toString());
                }) {
            @NonNull
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        requestQueue.add(logoutRequest);
    }


    private void clearInternalStorage() {
        File[] files = requireContext().getFilesDir().listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        // También puedes borrar otros directorios según sea necesario
        requireContext().getCacheDir().delete();
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Crear un archivo para guardar la imagen
            File photoFile = createImageFile();

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.uth.proyecto.fileprovider",
                        photoFile
                );

                // Pasar la URI del archivo a la intención de la cámara
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Iniciar la actividad de la cámara
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        // Crear un nombre de archivo único basado en la marca de tiempo
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            // Crear el archivo en el directorio de imágenes
            File imageFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );

            // Guardar la ruta del archivo para su uso posterior
            currentPhotoPath = imageFile.getAbsolutePath();
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // La imagen ha sido capturada exitosamente, ahora puedes mostrarla en tu ImageView o realizar otras acciones necesarias
            loadImageIntoImageView(currentPhotoPath);
        }
    }

    // Método para cargar la imagen capturada en el ImageView
    private void loadImageIntoImageView(String imagePath) {
        // Utiliza Glide u otra biblioteca para cargar la imagen en tu ImageView
        Glide.with(requireContext())
                .load(imagePath)
                .apply(new RequestOptions().transform(new CircleCrop()))
                .into(photo);
    }

}
